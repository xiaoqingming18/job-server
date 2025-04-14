package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.model.entity.LaborDemand;
import com.qingming.jobserver.model.vo.LaborDemandVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 劳务需求数据访问层
 */
@Mapper
public interface LaborDemandMapper extends BaseMapper<LaborDemand> {
    
    /**
     * 根据条件分页查询劳务需求
     * @param page 分页参数
     * @param projectId 项目ID
     * @param jobTypeId 工种ID
     * @param minDailyWage 最小日薪
     * @param maxDailyWage 最大日薪
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param accommodation 是否提供住宿
     * @param meals 是否提供餐食
     * @param status 需求状态
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> queryLaborDemandList(
            Page<LaborDemandVO> page,
            @Param("projectId") Integer projectId,
            @Param("jobTypeId") Integer jobTypeId,
            @Param("minDailyWage") BigDecimal minDailyWage,
            @Param("maxDailyWage") BigDecimal maxDailyWage,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("accommodation") Boolean accommodation,
            @Param("meals") Boolean meals,
            @Param("status") String status
    );
    
    /**
     * 根据ID查询劳务需求详情
     * @param id 需求ID
     * @return 劳务需求详情
     */
    LaborDemandVO getLaborDemandById(@Param("id") Integer id);
    
    /**
     * 获取特定项目的所有劳务需求
     * @param projectId 项目ID
     * @return 劳务需求列表
     */
    List<LaborDemandVO> getLaborDemandsByProjectId(@Param("projectId") Integer projectId);
}
