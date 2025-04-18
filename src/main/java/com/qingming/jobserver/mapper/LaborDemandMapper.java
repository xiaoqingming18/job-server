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
import java.util.Map;

/**
 * 劳务需求数据访问层
 */
@Mapper
public interface LaborDemandMapper extends BaseMapper<LaborDemand> {
    
    /**
     * 根据条件分页查询劳务需求
     * @param page 分页参数
     * @param projectId 项目ID
     * @param companyId 公司ID
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
            @Param("companyId") Integer companyId,
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
    
    /**
     * 根据关键词和地区等条件搜索劳务需求
     * @param page 分页参数
     * @param keyword 关键词
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param minDailyWage 最低日薪
     * @param maxDailyWage 最高日薪
     * @param startDateFrom 开始日期下限
     * @param startDateTo 开始日期上限
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> searchLaborDemands(
            Page<LaborDemandVO> page,
            @Param("keyword") String keyword,
            @Param("province") String province,
            @Param("city") String city,
            @Param("district") String district,
            @Param("minDailyWage") BigDecimal minDailyWage,
            @Param("maxDailyWage") BigDecimal maxDailyWage,
            @Param("startDateFrom") Date startDateFrom,
            @Param("startDateTo") Date startDateTo
    );
    
    /**
     * 获取推荐的劳务需求
     * @param limit 限制数量
     * @return 推荐的劳务需求列表
     */
    List<LaborDemandVO> getRecommendedLaborDemands(@Param("limit") Integer limit);
    
    /**
     * 获取项目的总人数
     * @param projectId 项目ID
     * @return 总人数
     */
    Integer getTotalHeadcountByProject(@Param("projectId") Integer projectId);
    
    /**
     * 计算项目的总成本
     * @param projectId 项目ID
     * @return 总成本
     */
    BigDecimal getTotalCostByProject(@Param("projectId") Integer projectId);
    
    /**
     * 获取项目的工种分布
     * @param projectId 项目ID
     * @return 工种分布信息列表
     */
    List<Map<String, Object>> getOccupationDistributionByProject(@Param("projectId") Integer projectId);
    
    /**
     * 获取公司的总需求数
     * @param companyId 公司ID
     * @return 总需求数
     */
    Integer getTotalDemandsByCompany(@Param("companyId") Integer companyId);
    
    /**
     * 获取公司的总人数
     * @param companyId 公司ID
     * @return 总人数
     */
    Integer getTotalHeadcountByCompany(@Param("companyId") Integer companyId);
    
    /**
     * 计算公司的总成本
     * @param companyId 公司ID
     * @return 总成本
     */
    BigDecimal getTotalCostByCompany(@Param("companyId") Integer companyId);
    
    /**
     * 获取公司的项目分布
     * @param companyId 公司ID
     * @return 项目分布信息列表
     */
    List<Map<String, Object>> getProjectDistributionByCompany(@Param("companyId") Integer companyId);
    
    /**
     * 获取工种需求热度统计
     * @param startDate 统计起始日期
     * @return 工种需求热度统计列表
     */
    List<Map<String, Object>> getOccupationHeatStats(@Param("startDate") Date startDate);
    
    /**
     * 根据工种获取劳务需求
     * @param page 分页参数
     * @param occupationId 工种ID
     * @param status 需求状态（可选）
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> getLaborDemandsByOccupation(
            Page<LaborDemandVO> page,
            @Param("occupationId") Integer occupationId,
            @Param("status") String status
    );
    
    /**
     * 根据地区获取劳务需求
     * @param page 分页参数
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> getLaborDemandsByLocation(
            Page<LaborDemandVO> page,
            @Param("province") String province,
            @Param("city") String city,
            @Param("district") String district
    );
    
    /**
     * 获取劳务需求最多的热门地区
     * @param limit 限制数量
     * @return 热门地区列表
     */
    List<Map<String, Object>> getHotDemandLocations(@Param("limit") Integer limit);
    
    /**
     * 获取地区的平均日薪
     * @param province 省份
     * @param city 城市
     * @return 平均日薪
     */
    BigDecimal getAverageWageByLocation(@Param("province") String province, @Param("city") String city);
}
