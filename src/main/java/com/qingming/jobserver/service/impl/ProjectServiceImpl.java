package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.project.ProjectAddDao;
import com.qingming.jobserver.model.dao.project.ProjectStatusUpdateDao;
import com.qingming.jobserver.model.dao.project.ProjectUpdateDao;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.ProjectInfoVO;
import com.qingming.jobserver.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 项目服务实现类
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public ProjectServiceImpl(ProjectMapper projectMapper, UserMapper userMapper) {
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
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

        // 3. 构建项目实体并设置字段
        ConstructionProject project = new ConstructionProject();
        BeanUtils.copyProperties(projectAddDao, project);
        
        // 4. 设置项目经理ID为当前用户ID
        project.setProjectManagerId(userId);
        
        // 5. 设置默认状态为待开工
        project.setStatus("pending");
        
        // 6. 执行插入操作
        int rows = projectMapper.insertProject(project);
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加项目失败");
        }
        
        // 7. 查询并返回刚添加的项目信息
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
     * 系统管理员、项目经理有权限更新项目
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
        
        // 非系统管理员且非项目经理，无权限
        throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION, "您无权操作此项目");
    }
}
