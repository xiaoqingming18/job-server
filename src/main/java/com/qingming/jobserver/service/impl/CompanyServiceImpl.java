package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.company.AddProjectManagerDao;
import com.qingming.jobserver.model.dao.company.CompanyQueryDao;
import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.dao.company.CompanyUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.AccountStatusEnum;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.CompanyInfoVO;
import com.qingming.jobserver.model.vo.ProjectManagerVO;
import com.qingming.jobserver.service.CompanyService;
import com.qingming.jobserver.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司服务实现类
 */
@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private ProjectService projectService;
    
    @Override
    @Transactional
    public User registerCompanyWithManager(CompanyRegisterDao registerDao) {
        // 1. 验证营业执照是否已被注册
        Company existingCompany = companyMapper.selectByLicenseNumber(registerDao.getLicenseNumber());
        if (existingCompany != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该营业执照编号已被注册");
        }
        
        // 2. 验证用户名是否存在
        User existingUser = userMapper.selectByUsername(registerDao.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.REGISTER_USER_EXIST, "该用户名已被注册");
        }
        
        // 3. 验证邮箱是否存在
        existingUser = userMapper.selectByEmail(registerDao.getEmail());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已被注册");
        }
        
        // 4. 创建公司
        Company company = new Company();
        company.setName(registerDao.getName());
        company.setLicenseNumber(registerDao.getLicenseNumber());
        company.setAddress(registerDao.getAddress());
        company.setLegalPerson(registerDao.getLegalPerson());
        company.setCreateTime(new Date());
        companyMapper.insertCompany(company);
        
        // 5. 获取最新的用户ID并为新用户递增ID
        Long latestUserId = userMapper.getLatestUserId();
        Long newUserId = latestUserId + 1;
        
        // 6. 创建企业管理员用户，使用指定的用户ID
        Date now = new Date();
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("id", newUserId);
        userParams.put("username", registerDao.getUsername());
        userParams.put("password", registerDao.getPassword());
        userParams.put("email", registerDao.getEmail());
        userParams.put("role", UserRoleEnum.company_admin.toString());
        userParams.put("accountStatus", AccountStatusEnum.enabled.toString());
        userParams.put("createTime", now);
        userMapper.insertProjectManagerUser(userParams); // 复用现有方法插入用户
        
        // 7. 创建用户对象用于返回
        User user = new User();
        user.setId(newUserId);
        user.setUsername(registerDao.getUsername());
        user.setPassword(registerDao.getPassword());
        user.setEmail(registerDao.getEmail());
        user.setRole(UserRoleEnum.company_admin);
        user.setAccountStatus(AccountStatusEnum.enabled);
        user.setCreateTime(now);
        
        // 8. 更新公司的管理员ID
        company.setAdminId(newUserId);
        companyMapper.updateAdminId(company.getId(), newUserId);
        
        return user;
    }
    
    @Override
    public Company getByLicenseNumber(String licenseNumber) {
        return companyMapper.selectByLicenseNumber(licenseNumber);
    }
    
    @Override
    public CompanyInfoVO getCompanyInfo(Integer companyId) {
        // 参数验证
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        // 查询企业详细信息
        CompanyInfoVO companyInfo = companyMapper.getCompanyInfoById(companyId);
        
        // 判断企业是否存在
        if (companyInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业信息不存在");
        }
        
        return companyInfo;
    }
    
    @Override
    @Transactional
    public CompanyInfoVO updateCompanyInfo(CompanyUpdateDao updateDao, Long userId) {
        // 参数验证
        if (updateDao == null || updateDao.getId() == null || updateDao.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        // 验证企业是否存在
        CompanyInfoVO existingCompany = companyMapper.getCompanyInfoById(updateDao.getId());
        if (existingCompany == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业信息不存在");
        }
        
        // 验证用户是否有权限修改企业信息（系统管理员、企业管理员或项目经理）
        if (!isSystemAdmin(userId) && !isCompanyAdmin(userId, updateDao.getId()) && !isProjectManagerOfCompany(userId, updateDao.getId())) {
            throw new BusinessException(ErrorCode.NO_COMPANY_AUTH, "无权修改企业信息");
        }
        
        // 构建更新对象，只更新非null字段
        Company company = new Company();
        company.setId(updateDao.getId());
        company.setName(updateDao.getName());
        company.setAddress(updateDao.getAddress());
        company.setLegalPerson(updateDao.getLegalPerson());
        
        // 执行更新操作
        int rows = companyMapper.updateCompanySelective(company);
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新企业信息失败");
        }
        
        // 返回更新后的企业信息
        return companyMapper.getCompanyInfoById(updateDao.getId());
    }
    
    @Override
    public boolean isProjectManagerOfCompany(Long userId, Integer companyId) {
        if (userId == null || userId <= 0 || companyId == null || companyId <= 0) {
            return false;
        }
        
        // 查询用户是否为企业的项目经理
        Integer count = companyMapper.countProjectManagerByUserIdAndCompanyId(userId, companyId);
        return count != null && count > 0;
    }
    
    @Override
    public boolean isCompanyAdmin(Long userId, Integer companyId) {
        if (userId == null || userId <= 0 || companyId == null || companyId <= 0) {
            return false;
        }
        
        // 查询公司信息
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            return false;
        }
        
        // 检查当前用户是否是该公司的管理员
        return userId.equals(company.getAdminId());
    }
    
    @Override
    public boolean isSystemAdmin(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }
        
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 检查用户角色是否为系统管理员
        return UserRoleEnum.system_admin.equals(user.getRole());
    }
    
    @Override
    @Transactional
    public boolean addProjectManager(AddProjectManagerDao addProjectManagerDao, Long currentUserId) {
        // 1. 验证参数
        if (addProjectManagerDao == null || addProjectManagerDao.getCompanyId() == null || 
            addProjectManagerDao.getUsername() == null || addProjectManagerDao.getPassword() == null || 
            addProjectManagerDao.getEmail() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        
        // 2. 验证企业是否存在
        Company company = companyMapper.selectById(addProjectManagerDao.getCompanyId());
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业不存在");
        }
        
        // 添加日志，记录当前用户ID和企业的adminId，以便排查问题
        log.info("添加项目经理 - 当前用户ID: {}, 企业ID: {}, 企业管理员ID: {}", 
                currentUserId, addProjectManagerDao.getCompanyId(), company.getAdminId());
        
        // 3. 验证当前用户权限 - 系统管理员或企业管理员都可以添加项目经理
        boolean isAdmin = isSystemAdmin(currentUserId) || isCompanyAdmin(currentUserId, addProjectManagerDao.getCompanyId());
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.NO_COMPANY_AUTH, "只有企业管理员或系统管理员才能添加项目经理");
        }
        
        // 4. 验证用户名是否已存在
        User existingUser = userMapper.selectByUsername(addProjectManagerDao.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.REGISTER_USER_EXIST, "用户名已被注册");
        }
        
        // 5. 验证邮箱是否已存在
        existingUser = userMapper.selectByEmail(addProjectManagerDao.getEmail());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已被注册");
        }
        
        // 6. 获取最新的用户ID并为新用户递增ID
        Long latestUserId = userMapper.getLatestUserId();
        Long newUserId = latestUserId + 1;
        
        // 7. 创建项目经理用户
        Date now = new Date();
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("id", newUserId);
        userParams.put("username", addProjectManagerDao.getUsername());
        userParams.put("password", addProjectManagerDao.getPassword());
        userParams.put("email", addProjectManagerDao.getEmail());
        userParams.put("role", UserRoleEnum.project_manager.toString());
        userParams.put("accountStatus", AccountStatusEnum.enabled.toString());
        userParams.put("createTime", now);
        userMapper.insertProjectManagerUser(userParams);
        
        // 8. 添加项目经理与企业的关联
        companyMapper.insertProjectManager(newUserId, addProjectManagerDao.getCompanyId(), 
                addProjectManagerDao.getPosition() != null ? addProjectManagerDao.getPosition() : "项目经理");
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteCompany(Integer companyId, Long currentUserId) {
        // 1. 参数验证
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        if (currentUserId == null || currentUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        
        // 2. 验证企业是否存在
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业不存在");
        }
        
        // 3. 验证用户权限
        if (!isSystemAdmin(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只有系统管理员才能删除企业");
        }
        
        // 4. 检查企业是否有关联项目，如果有则不允许删除
        int projectCount = companyMapper.countRelatedProjects(companyId);
        if (projectCount > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "该企业存在关联的项目数据，无法删除");
        }
        
        // 5. 先删除企业相关的项目经理关联记录
        companyMapper.deleteProjectManagersByCompanyId(companyId);
        
        // 6. 删除企业记录
        int result = companyMapper.deleteCompanyById(companyId);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean forceDeleteCompany(Integer companyId, Long currentUserId) {
        // 1. 参数验证
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        if (currentUserId == null || currentUserId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        
        // 2. 验证企业是否存在
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业不存在");
        }
        
        // 3. 验证用户权限（仅限系统管理员）
        if (!isSystemAdmin(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只有系统管理员才能强制删除企业");
        }
        
        // 4. 获取企业下所有项目ID
        List<Integer> projectIds = projectMapper.getProjectIdsByCompanyId(companyId);
        
        // 5. 删除企业下所有项目
        int deletedProjectCount = 0;
        if (!projectIds.isEmpty()) {
            log.info("开始强制删除企业[{}]下的所有项目，共{}个项目", companyId, projectIds.size());
            
            for (Integer projectId : projectIds) {
                try {
                    // 尝试删除项目，忽略劳务需求关联检查
                    int result = projectMapper.deleteProjectById(projectId);
                    if (result > 0) {
                        deletedProjectCount++;
                    }
                } catch (Exception e) {
                    log.error("删除项目[{}]失败：{}", projectId, e.getMessage());
                }
            }
            
            log.info("成功删除企业[{}]下的{}个项目", companyId, deletedProjectCount);
        }
        
        // 6. 删除企业相关的项目经理关联记录
        companyMapper.deleteProjectManagersByCompanyId(companyId);
        
        // 7. 删除企业记录
        int result = companyMapper.deleteCompanyById(companyId);
        
        return result > 0;
    }
    
    @Override
    public Page<CompanyInfoVO> queryCompanyList(CompanyQueryDao queryDao) {
        // 参数校验
        if (queryDao == null) {
            queryDao = new CompanyQueryDao();
        }
        
        // 创建分页对象
        Page<CompanyInfoVO> page = new Page<>(queryDao.getPageNum(), queryDao.getPageSize());
        
        // 调用Mapper执行查询
        return companyMapper.queryCompanyList(page, queryDao.getName());
    }
    
    @Override
    public List<ProjectManagerVO> getCompanyManagerList(Integer companyId) {
        // 参数验证
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        // 验证企业是否存在
        CompanyInfoVO existingCompany = companyMapper.getCompanyInfoById(companyId);
        if (existingCompany == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业信息不存在");
        }
        
        // 查询企业的项目经理列表
        List<ProjectManagerVO> managerList = companyMapper.getProjectManagersByCompanyId(companyId);
        
        return managerList;
    }
}
