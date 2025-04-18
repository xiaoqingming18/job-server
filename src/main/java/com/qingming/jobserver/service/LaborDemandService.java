package com.qingming.jobserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.vo.LaborDemandVO;

import java.util.List;
import java.util.Map;

/**
 * 劳务需求服务接口
 */
public interface LaborDemandService {
    
    /**
     * 添加劳务需求
     * @param laborDemandAddDao 劳务需求添加参数
     * @param userId 当前用户ID
     * @return 添加成功的劳务需求信息
     */
    LaborDemandVO addLaborDemand(LaborDemandAddDao laborDemandAddDao, Long userId);
    
    /**
     * 根据ID获取劳务需求详情
     * @param demandId 劳务需求ID
     * @return 劳务需求详细信息
     */
    LaborDemandVO getLaborDemandInfo(Integer demandId);
    
    /**
     * 更新劳务需求信息
     * @param laborDemandUpdateDao 劳务需求更新参数
     * @param userId 当前用户ID
     * @return 更新后的劳务需求信息
     */
    LaborDemandVO updateLaborDemand(LaborDemandUpdateDao laborDemandUpdateDao, Long userId);
    
    /**
     * 更新劳务需求状态
     * @param statusUpdateDao 劳务需求状态更新参数
     * @param userId 当前用户ID
     * @return 更新后的劳务需求信息
     */
    LaborDemandVO updateLaborDemandStatus(LaborDemandStatusUpdateDao statusUpdateDao, Long userId);
    
    /**
     * 删除劳务需求
     * @param demandId 劳务需求ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    Boolean deleteLaborDemand(Integer demandId, Long userId);
    
    /**
     * 分页查询劳务需求
     * @param queryDao 查询参数
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> queryLaborDemandList(LaborDemandQueryDao queryDao);
    
    /**
     * 获取特定项目的所有劳务需求
     * @param projectId 项目ID
     * @return 劳务需求列表
     */
    List<LaborDemandVO> getLaborDemandsByProjectId(Integer projectId);
    
    /**
     * 检查用户是否有权限对劳务需求进行修改操作
     * @param demandId 劳务需求ID
     * @param userId 用户ID
     * @return 是否有操作权限
     */
    Boolean checkOperationPermission(Integer demandId, Long userId);
    
    /**
     * 根据关键词和地区等条件搜索劳务需求
     * @param keyword 关键词
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param minDailyWage 最低日薪
     * @param maxDailyWage 最高日薪
     * @param startDateFrom 开始日期下限
     * @param startDateTo 开始日期上限
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> searchLaborDemands(String keyword, String province, String city, 
                                        String district, Double minDailyWage, Double maxDailyWage, 
                                        String startDateFrom, String startDateTo, 
                                        Integer page, Integer size);
    
    /**
     * 获取推荐的劳务需求
     * @param limit 限制数量
     * @return 推荐的劳务需求列表
     */
    List<LaborDemandVO> getRecommendedLaborDemands(Integer limit);
    
    /**
     * 获取特定项目的劳务需求统计信息
     * @param projectId 项目ID
     * @return 项目需求统计信息
     */
    Map<String, Object> getProjectDemandStats(Integer projectId);
    
    /**
     * 获取特定公司的劳务需求统计信息
     * @param companyId 公司ID
     * @return 公司需求统计信息
     */
    Map<String, Object> getCompanyDemandStats(Integer companyId);
    
    /**
     * 分析工种需求热度
     * @param period 统计周期（天）
     * @return 工种需求热度分析数据
     */
    List<Map<String, Object>> analyzeOccupationHeat(Integer period);
    
    /**
     * 根据工种ID获取劳务需求
     * @param occupationId 工种ID
     * @param status 需求状态
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> getLaborDemandsByOccupation(Integer occupationId, String status, Integer page, Integer size);
    
    /**
     * 根据项目特征和需求描述推荐合适的工种
     * @param projectFeatures 项目特征
     * @param demandDescription 需求描述
     * @return 推荐的工种列表及匹配度
     */
    List<Map<String, Object>> suggestOccupations(Map<String, Object> projectFeatures, String demandDescription);
    
    /**
     * 根据地区获取劳务需求
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> getLaborDemandsByLocation(String province, String city, String district, Integer page, Integer size);
    
    /**
     * 获取劳务需求最多的热门地区
     * @param limit 限制数量
     * @return 热门地区信息列表
     */
    List<Map<String, Object>> getHotDemandLocations(Integer limit);
    
    /**
     * 根据项目类型、工种和地区综合查询劳务需求
     * @param projectType 项目类型ID
     * @param occupationId 工种ID
     * @param location 地区（省/市/区）
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> queryLaborDemandsByCriteria(Integer projectType, Integer occupationId, String location, Integer page, Integer size);
}
