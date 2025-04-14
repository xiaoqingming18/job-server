package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.LaborDemandMapper;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.entity.LaborDemand;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.LaborDemandVO;
import com.qingming.jobserver.service.LaborDemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 劳务需求服务实现类
 */
@Slf4j
@Service
public class LaborDemandServiceImpl extends ServiceImpl<LaborDemandMapper, LaborDemand> implements LaborDemandService {    private final LaborDemandMapper laborDemandMapper;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    
    public LaborDemandServiceImpl(LaborDemandMapper laborDemandMapper, ProjectMapper projectMapper, UserMapper userMapper, CompanyMapper companyMapper) {
        this.laborDemandMapper = laborDemandMapper;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO addLaborDemand(LaborDemandAddDao laborDemandAddDao, Long userId) {
        // 1. 检查项目是否存在
        ConstructionProject project = projectMapper.selectById(laborDemandAddDao.getProjectId());
        if (project == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "项目不存在");
        }
        
        // 2. 检查用户是否有权限操作该项目
        if (!hasProjectPermission(project, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限操作此项目");
        }
        
        // 3. 检查日期参数逻辑
        Date startDate = laborDemandAddDao.getStartDate();
        Date endDate = laborDemandAddDao.getEndDate();
        if (startDate.after(endDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "开始日期不能晚于结束日期");
        }
        
        // 4. 构建劳务需求实体并保存
        LaborDemand laborDemand = new LaborDemand();
        BeanUtils.copyProperties(laborDemandAddDao, laborDemand);
        laborDemand.setStatus("open"); // 设置初始状态为开放中
        laborDemand.setCreateTime(new Date());
        
        // 5. 插入数据
        this.save(laborDemand);
        
        // 6. 查询并返回完整的劳务需求信息
        return this.getLaborDemandInfo(laborDemand.getId());
    }
    
    @Override
    public LaborDemandVO getLaborDemandInfo(Integer demandId) {
        // 查询劳务需求详情
        LaborDemandVO laborDemandVO = laborDemandMapper.getLaborDemandById(demandId);
        if (laborDemandVO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        return laborDemandVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO updateLaborDemand(LaborDemandUpdateDao laborDemandUpdateDao, Long userId) {
        // 1. 检查劳务需求是否存在
        Integer demandId = laborDemandUpdateDao.getId();
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限修改此劳务需求");
        }
        
        // 3. 如果更新了开始和结束日期，检查日期逻辑
        Date startDate = laborDemandUpdateDao.getStartDate();
        Date endDate = laborDemandUpdateDao.getEndDate();
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "开始日期不能晚于结束日期");
        }
        
        // 4. 更新内容
        LaborDemand updateDemand = new LaborDemand();
        BeanUtils.copyProperties(laborDemandUpdateDao, updateDemand);
        updateDemand.setUpdateTime(new Date());
        
        // 5. 更新数据
        this.updateById(updateDemand);
        
        // 6. 查询并返回更新后的完整劳务需求信息
        return this.getLaborDemandInfo(demandId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO updateLaborDemandStatus(LaborDemandStatusUpdateDao statusUpdateDao, Long userId) {
        // 1. 检查劳务需求是否存在
        Integer demandId = statusUpdateDao.getId();
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限修改此劳务需求状态");
        }
        
        // 3. 检查状态参数是否有效
        String status = statusUpdateDao.getStatus();
        if (!("open".equals(status) || "filled".equals(status) || "cancelled".equals(status) || "expired".equals(status))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "无效的状态值");
        }
        
        // 4. 更新状态
        LambdaUpdateWrapper<LaborDemand> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LaborDemand::getId, demandId)
                .set(LaborDemand::getStatus, status)
                .set(LaborDemand::getUpdateTime, new Date());
        
        this.update(updateWrapper);
        
        // 5. 查询并返回更新后的完整劳务需求信息
        return this.getLaborDemandInfo(demandId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteLaborDemand(Integer demandId, Long userId) {
        // 1. 检查劳务需求是否存在
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限删除此劳务需求");
        }
        
        // 3. 执行删除操作
        return this.removeById(demandId);
    }
    
    @Override
    public Page<LaborDemandVO> queryLaborDemandList(LaborDemandQueryDao queryDao) {
        // 创建分页对象
        Page<LaborDemandVO> page = new Page<>(queryDao.getPageNum(), queryDao.getPageSize());
        
        // 调用Mapper查询
        return laborDemandMapper.queryLaborDemandList(
                page,
                queryDao.getProjectId(),
                queryDao.getJobTypeId(),
                queryDao.getMinDailyWage(),
                queryDao.getMaxDailyWage(),
                queryDao.getStartDate(),
                queryDao.getEndDate(),
                queryDao.getAccommodation(),
                queryDao.getMeals(),
                queryDao.getStatus()
        );
    }
    
    @Override
    public List<LaborDemandVO> getLaborDemandsByProjectId(Integer projectId) {
        // 检查项目是否存在
        ConstructionProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "项目不存在");
        }
        
        // 查询并返回指定项目的所有劳务需求
        return laborDemandMapper.getLaborDemandsByProjectId(projectId);
    }
    
    @Override
    public Boolean checkOperationPermission(Integer demandId, Long userId) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;        }
        
        // 系统管理员拥有所有权限
        if (UserRoleEnum.system_admin.equals(user.getRole())) {
            return true;
        }
        
        // 获取需求信息
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            return false;
        }
        
        // 获取项目信息
        ConstructionProject project = projectMapper.selectById(laborDemand.getProjectId());
        if (project == null) {
            return false;
        }
        
        // 项目经理有权限
        if (UserRoleEnum.project_manager.equals(user.getRole()) && userId.equals(project.getProjectManagerId())) {
            return true;
        }
        
        // 企业管理员有权限
        if (UserRoleEnum.company_admin.equals(user.getRole())) {
            // 检查用户是否为该项目所属企业的管理员
            Company company = companyMapper.selectById(project.getCompanyId());
            if (company != null && userId.equals(company.getAdminId())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检查用户是否有权限操作项目
     * @param project 项目信息
     * @param userId 用户ID
     * @return 是否有权限
     */
    private boolean hasProjectPermission(ConstructionProject project, Long userId) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;        }
        
        // A. 系统管理员拥有所有权限
        if (UserRoleEnum.system_admin.equals(user.getRole())) {
            return true;
        }
        
        // B. 项目经理有权限
        if (UserRoleEnum.project_manager.equals(user.getRole()) && userId.equals(project.getProjectManagerId())) {
            return true;
        }
        
        // C. 企业管理员有权限
        if (UserRoleEnum.company_admin.equals(user.getRole())) {
            // 检查用户是否为该项目所属企业的管理员
            Company company = companyMapper.selectById(project.getCompanyId());
            if (company != null && userId.equals(company.getAdminId())) {
                return true;
            }
        }
        
        return false;
    }
}
