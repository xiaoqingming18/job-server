package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.project.ProjectAddDao;
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
}
