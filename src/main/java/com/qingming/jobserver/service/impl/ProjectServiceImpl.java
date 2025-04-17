package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.project.ProjectAddDao;
import com.qingming.jobserver.model.dao.project.ProjectPageRequestDao;
import com.qingming.jobserver.model.dao.project.ProjectStatusUpdateDao;
import com.qingming.jobserver.model.dao.project.ProjectUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.PageResult;
import com.qingming.jobserver.model.vo.ProjectInfoVO;
import com.qingming.jobserver.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 项目服务实现类
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProjectServiceImpl(ProjectMapper projectMapper, UserMapper userMapper, CompanyMapper companyMapper) {
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }

    @Override
    @Transactional
    public ProjectInfoVO addProject(ProjectAddDao projectAddDao, Long userId) {
        // 1. 校验用户是否存在并获取角色
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户不存在或未登录");
        }
        
        // 2. 校验用户角色，求职者不允许添加项目
        if (UserRoleEnum.job_seeker.name().equals(user.getRole().name())) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "您所属的用户组不允许添加项目");
        }
        
        // 3. 校验项目经理ID是否有效
        if (projectAddDao.getProjectManagerId() == null || projectAddDao.getProjectManagerId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目经理ID无效");
        }
        
        // 4. 验证项目经理是否存在
        User projectManager = userMapper.selectById(projectAddDao.getProjectManagerId());
        if (projectManager == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目经理不存在");
        }
        
        // 5. 验证指定用户是否为项目经理角色
        if (!UserRoleEnum.project_manager.equals(projectManager.getRole())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "指定用户不是项目经理角色");
        }

        // 6. 构建项目实体并设置字段
        ConstructionProject project = new ConstructionProject();
        BeanUtils.copyProperties(projectAddDao, project);
        
        // 7. 设置默认状态为待开工
        project.setStatus("pending");
        
        // 8. 执行插入操作
        int rows = projectMapper.insertProject(project);
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加项目失败");
        }
        
        // 9. 查询并返回刚添加的项目信息
        return projectMapper.getProjectInfoById(project.getId());
    }

    @Override
    public ProjectInfoVO getProjectInfo(Integer projectId) {
        if (projectId == null || projectId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目ID无效");
        }
        ProjectInfoVO projectInfo = projectMapper.getProjectInfoById(projectId);
        if (projectInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目不存在");
        }
        return projectInfo;
    }

    @Override
    @Transactional
    public ProjectInfoVO updateProject(ProjectUpdateDao projectUpdateDao, Long userId) {
        // 1. 参数校验
        if (projectUpdateDao == null || projectUpdateDao.getId() == null || projectUpdateDao.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目ID无效");
        }
        
        // 2. 检查项目是否存在
        ProjectInfoVO existingProject = projectMapper.getProjectInfoById(projectUpdateDao.getId());
        if (existingProject == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目不存在");
        }
        
        // 3. 校验用户是否有权限更新项目
        checkProjectUpdatePermission(userId, existingProject.getId(), existingProject.getProjectManagerId());
        
        // 4. 构建更新实体对象
        ConstructionProject project = new ConstructionProject();
        BeanUtils.copyProperties(projectUpdateDao, project);
        
        // 5. 执行更新操作
        int rows = projectMapper.updateProjectSelective(project);
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新项目信息失败");
        }
        
        // 6. 查询并返回更新后的项目信息
        return projectMapper.getProjectInfoById(project.getId());
    }

    @Override
    @Transactional
    public ProjectInfoVO updateProjectStatus(ProjectStatusUpdateDao statusUpdateDao, Long userId) {
        // 1. 参数校验
        if (statusUpdateDao == null || statusUpdateDao.getId() == null || statusUpdateDao.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目ID无效");
        }
        
        // 2. 检查项目是否存在
        ProjectInfoVO existingProject = projectMapper.getProjectInfoById(statusUpdateDao.getId());
        if (existingProject == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目不存在");
        }
        
        // 3. 校验用户是否有权限更新项目状态
        checkProjectUpdatePermission(userId, existingProject.getId(), existingProject.getProjectManagerId());
        
        // 4. 执行状态更新操作
        int rows = projectMapper.updateProjectStatus(statusUpdateDao.getId(), statusUpdateDao.getStatus());
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新项目状态失败");
        }
        
        // 5. 查询并返回更新后的项目信息
        return projectMapper.getProjectInfoById(statusUpdateDao.getId());
    }
    
    @Override
    @Transactional
    public boolean deleteProject(Integer projectId, Long userId) {
        // 1. 参数校验
        if (projectId == null || projectId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目ID无效");
        }
        
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        
        // 2. 检查项目是否存在
        ProjectInfoVO existingProject = projectMapper.getProjectInfoById(projectId);
        if (existingProject == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目不存在");
        }
        
        // 3. 校验用户是否有权限删除项目（仅系统管理员和项目经理可以删除）
        checkProjectUpdatePermission(userId, existingProject.getId(), existingProject.getProjectManagerId());
        
        // 4. 检查项目是否有关联的劳务需求，如果有则不允许删除
        int laborDemandCount = projectMapper.countRelatedLaborDemands(projectId);
        if (laborDemandCount > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "该项目存在关联的劳务需求数据，无法删除");
        }
        
        // 5. 执行删除操作
        int result = projectMapper.deleteProjectById(projectId);
        
        return result > 0;
    }
    
    /**
     * 检查用户是否有权限更新项目
     * 系统管理员、企业管理员和项目经理有权限更新项目
     * @param userId 当前用户ID
     * @param projectId 项目ID
     * @param projectManagerId 项目经理ID
     */
    private void checkProjectUpdatePermission(Long userId, Integer projectId, Long projectManagerId) {
        // 获取当前用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户不存在或未登录");
        }
        
        // 系统管理员有权限
        if (UserRoleEnum.system_admin.name().equals(user.getRole().name())) {
            return;
        }
        
        // 项目经理本人有权限
        if (Objects.equals(userId, projectManagerId)) {
            return;
        }

        // 企业管理员有权限
        if (UserRoleEnum.company_admin.name().equals(user.getRole().name())) {
            // 获取项目信息
            ProjectInfoVO project = projectMapper.getProjectInfoById(projectId);
            if (project == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "项目不存在");
            }
            
            // 获取企业信息
            Company company = companyMapper.selectById(project.getCompanyId());
            if (company != null && Objects.equals(userId, company.getAdminId())) {
                return;
            }
        }
        
        // 其他情况，无权限
        throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "您无权操作此项目");
    }

    @Override
    public List<ProjectInfoVO> getCompanyProjectList(Integer companyId, Long userId) {
        // 1. 参数校验
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID无效");
        }
        
        // 2. 获取当前用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户不存在或未登录");
        }
        
        // 3. 检查用户权限
        if (UserRoleEnum.system_admin.name().equals(user.getRole().name())) {
            // 系统管理员直接放行
            return projectMapper.getProjectListByCompanyId(companyId);
        }
        
        // 4. 检查是否为企业管理员
        if (!UserRoleEnum.company_admin.name().equals(user.getRole().name())) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "所属用户组无查看企业项目列表权限");
        }
        
        // 5. 检查用户是否为该企业的管理员
        Company company = companyMapper.selectById(companyId);
        if (company == null || !Objects.equals(company.getAdminId(), userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "所属用户组无查看企业项目列表权限");
        }
        
        // 6. 查询并返回企业项目列表
        return projectMapper.getProjectListByCompanyId(companyId);
    }

    @Override
    public PageResult<ProjectInfoVO> getProjectPage(ProjectPageRequestDao requestDao) {
        // 创建分页结果对象
        PageResult<ProjectInfoVO> pageResult = new PageResult<>();
        pageResult.setPageNum(requestDao.getPageNum());
        pageResult.setPageSize(requestDao.getPageSize());

        try {
            // 构建查询条件
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM construction_project WHERE 1=1");

            // 添加筛选条件
            List<Object> params = new ArrayList<>();
            if (StringUtils.hasText(requestDao.getStatus())) {
                sqlBuilder.append(" AND status = ?");
                params.add(requestDao.getStatus());
            }
            if (StringUtils.hasText(requestDao.getProjectType())) {
                sqlBuilder.append(" AND project_type = ?");
                params.add(requestDao.getProjectType());
            }
            if (StringUtils.hasText(requestDao.getProvince())) {
                sqlBuilder.append(" AND province = ?");
                params.add(requestDao.getProvince());
            }
            if (StringUtils.hasText(requestDao.getCity())) {
                sqlBuilder.append(" AND city = ?");
                params.add(requestDao.getCity());
            }

            // 添加排序
            String orderBy = StringUtils.hasText(requestDao.getOrderBy()) ? 
                requestDao.getOrderBy() : "create_time";
            String orderDirection = "asc".equalsIgnoreCase(requestDao.getOrderDirection()) ? 
                "asc" : "desc";
            sqlBuilder.append(" ORDER BY ").append(orderBy).append(" ").append(orderDirection);

            // 计算总记录数
            String countSql = "SELECT COUNT(*) FROM (" + sqlBuilder.toString() + ") t";
            Long total = jdbcTemplate.queryForObject(countSql, Long.class, params.toArray());
            pageResult.setTotal(total);

            // 计算总页数
            int totalPages = (int) Math.ceil((double) total / requestDao.getPageSize());
            pageResult.setTotalPages(totalPages);

            // 添加分页
            sqlBuilder.append(" LIMIT ?, ?");
            params.add((requestDao.getPageNum() - 1) * requestDao.getPageSize());
            params.add(requestDao.getPageSize());

            // 执行查询
            List<ProjectInfoVO> projectList = jdbcTemplate.query(
                sqlBuilder.toString(),
                params.toArray(),
                new BeanPropertyRowMapper<>(ProjectInfoVO.class)
            );

            pageResult.setList(projectList);
            return pageResult;
        } catch (Exception e) {
            log.error("分页查询项目列表失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "分页查询项目列表失败");
        }
    }
    
    @Override
    public List<ProjectInfoVO> getManagerProjectList(Long projectManagerId) {
        // 1. 参数校验
        if (projectManagerId == null || projectManagerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "项目经理ID无效");
        }
        
        // 2. 验证项目经理是否存在
        User manager = userMapper.selectById(projectManagerId);
        if (manager == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "项目经理不存在");
        }
        
        // 3. 验证用户是否为项目经理角色
        if (!UserRoleEnum.project_manager.equals(manager.getRole())) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "该用户不是项目经理");
        }
        
        // 4. 查询并返回项目经理管理的项目列表
        return projectMapper.getProjectListByManagerId(projectManagerId);
    }
}
