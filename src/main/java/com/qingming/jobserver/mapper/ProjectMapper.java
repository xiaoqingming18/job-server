package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.vo.ProjectInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    
    /**
     * 更新项目信息（仅更新非null字段）
     * @param project 项目实体对象
     * @return 影响行数
     */
    int updateProjectSelective(ConstructionProject project);
    
    /**
     * 更新项目状态
     * @param projectId 项目ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateProjectStatus(@Param("projectId") Integer projectId, @Param("status") String status);
    
    /**
     * 检查用户是否为项目的项目经理
     * @param userId 用户ID
     * @param projectId 项目ID
     * @return 1表示是项目经理，0表示不是
     */
    Integer isProjectManager(@Param("userId") Long userId, @Param("projectId") Integer projectId);
    
    /**
     * 删除项目
     * @param projectId 项目ID
     * @return 影响行数
     */
    int deleteProjectById(@Param("projectId") Integer projectId);
    
    /**
     * 检查项目是否有关联的劳务需求
     * @param projectId 项目ID
     * @return 关联的劳务需求数量
     */
    int countRelatedLaborDemands(@Param("projectId") Integer projectId);
    
    /**
     * 根据企业ID查询项目ID列表
     * @param companyId 企业ID
     * @return 项目ID列表
     */
    List<Integer> getProjectIdsByCompanyId(@Param("companyId") Integer companyId);
}
