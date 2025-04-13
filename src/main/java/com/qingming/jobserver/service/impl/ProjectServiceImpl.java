package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.project.ProjectAddDao;
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
}
