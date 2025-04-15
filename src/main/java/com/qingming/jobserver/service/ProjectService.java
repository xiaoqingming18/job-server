package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.project.ProjectAddDao;
import com.qingming.jobserver.model.dao.project.ProjectStatusUpdateDao;
import com.qingming.jobserver.model.dao.project.ProjectUpdateDao;
import com.qingming.jobserver.model.vo.ProjectInfoVO;

/**
 * 项目服务接口
 */
public interface ProjectService {
    
    /**
     * 添加项目
     * @param projectAddDao 项目添加参数
     * @param userId 当前用户ID
     * @return 添加成功的项目信息
     */
    ProjectInfoVO addProject(ProjectAddDao projectAddDao, Long userId);
    
    /**
     * 根据ID获取项目详情
     * @param projectId 项目ID
     * @return 项目详细信息
     */
    ProjectInfoVO getProjectInfo(Integer projectId);
    
    /**
     * 更新项目信息
     * @param projectUpdateDao 项目更新参数
     * @param userId 当前用户ID
     * @return 更新后的项目信息
     */
    ProjectInfoVO updateProject(ProjectUpdateDao projectUpdateDao, Long userId);
    
    /**
     * 更新项目状态
     * @param statusUpdateDao 项目状态更新参数
     * @param userId 当前用户ID
     * @return 更新后的项目信息
     */
    ProjectInfoVO updateProjectStatus(ProjectStatusUpdateDao statusUpdateDao, Long userId);
    
    /**
     * 删除项目
     * @param projectId 项目ID
     * @param userId 当前用户ID
     * @return 删除成功返回true，否则返回false
     */
    boolean deleteProject(Integer projectId, Long userId);
}
