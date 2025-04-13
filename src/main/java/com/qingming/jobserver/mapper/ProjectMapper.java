package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.vo.ProjectInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * 建筑项目Mapper接口
 */
public interface ProjectMapper extends BaseMapper<ConstructionProject> {
    
    /**
     * 插入项目信息
     * @param project 项目实体对象
     * @return 影响行数
     */
    int insertProject(ConstructionProject project);
    
    /**
     * 根据ID查询项目详细信息
     * @param projectId 项目ID
     * @return 项目详细信息VO
     */
    ProjectInfoVO getProjectInfoById(@Param("projectId") Integer projectId);
}
