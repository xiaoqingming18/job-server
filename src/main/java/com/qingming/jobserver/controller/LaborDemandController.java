package com.qingming.jobserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.vo.LaborDemandVO;
import com.qingming.jobserver.service.LaborDemandService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 劳务需求控制器
 */
@Slf4j
@RestController
@RequestMapping("/labor-demand")
public class LaborDemandController {
    
    private final LaborDemandService laborDemandService;
    
    @Autowired
    public LaborDemandController(LaborDemandService laborDemandService) {
        this.laborDemandService = laborDemandService;
    }
    
    /**
     * 添加劳务需求
     * @param laborDemandAddDao 劳务需求信息
     * @return 添加的劳务需求信息
     */
    @PostMapping("/add")
    public BaseResponse<LaborDemandVO> addLaborDemand(@RequestBody @Valid LaborDemandAddDao laborDemandAddDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层添加劳务需求
            LaborDemandVO laborDemandVO = laborDemandService.addLaborDemand(laborDemandAddDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("添加劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("添加劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "添加劳务需求失败，系统异常");
        }
    }
    
    /**
     * 创建劳务需求（新接口，与add功能相同，保持接口命名一致性）
     * @param laborDemandAddDao 劳务需求信息
     * @return 添加的劳务需求信息
     */
    @PostMapping("/create")
    public BaseResponse<LaborDemandVO> createLaborDemand(@RequestBody @Valid LaborDemandAddDao laborDemandAddDao) {
        return addLaborDemand(laborDemandAddDao);
    }
    
    /**
     * 获取劳务需求详情
     * @param id 劳务需求ID
     * @return 劳务需求详细信息
     */
    @GetMapping("/info/{id}")
    public BaseResponse<LaborDemandVO> getLaborDemandInfo(@PathVariable("id") Integer id) {
        try {
            // 调用服务层获取劳务需求信息
            LaborDemandVO laborDemandVO = laborDemandService.getLaborDemandInfo(id);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取劳务需求信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取劳务需求信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取劳务需求信息失败，系统异常");
        }
    }
    
    /**
     * 获取劳务需求详情（对应原设计中的/{id}）
     * @param id 劳务需求ID
     * @return 劳务需求详细信息
     */
    @GetMapping(value = "/{id:[\\d]+}")
    public BaseResponse<LaborDemandVO> getLaborDemand(@PathVariable("id") Integer id) {
        return getLaborDemandInfo(id);
    }
    
    /**
     * 更新劳务需求信息
     * @param laborDemandUpdateDao 劳务需求更新信息
     * @return 更新后的劳务需求信息
     */
    @PutMapping("/update")
    public BaseResponse<LaborDemandVO> updateLaborDemand(@RequestBody @Valid LaborDemandUpdateDao laborDemandUpdateDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层更新劳务需求
            LaborDemandVO laborDemandVO = laborDemandService.updateLaborDemand(laborDemandUpdateDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("更新劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("更新劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "更新劳务需求失败，系统异常");
        }
    }
    
    /**
     * 更新劳务需求状态
     * @param statusUpdateDao 劳务需求状态更新信息
     * @return 更新后的劳务需求信息
     */
    @PutMapping("/status")
    public BaseResponse<LaborDemandVO> updateLaborDemandStatus(@RequestBody @Valid LaborDemandStatusUpdateDao statusUpdateDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层更新劳务需求状态
            LaborDemandVO laborDemandVO = laborDemandService.updateLaborDemandStatus(statusUpdateDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("更新劳务需求状态失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("更新劳务需求状态发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "更新劳务需求状态失败，系统异常");
        }
    }
    
    /**
     * 删除劳务需求
     * @param id 劳务需求ID
     * @return 是否删除成功
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteLaborDemand(@PathVariable("id") Integer id) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层删除劳务需求
            Boolean result = laborDemandService.deleteLaborDemand(id, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(result);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("删除劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("删除劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "删除劳务需求失败，系统异常");
        }
    }
    
    /**
     * 分页查询劳务需求
     * @param queryDao 查询参数
     * @return 劳务需求分页列表
     */
    @PostMapping("/list")
    public BaseResponse<Page<LaborDemandVO>> listLaborDemands(@RequestBody LaborDemandQueryDao queryDao) {
        try {
            // 调用服务层查询劳务需求列表
            Page<LaborDemandVO> laborDemandPage = laborDemandService.queryLaborDemandList(queryDao);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandPage);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("查询劳务需求列表失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("查询劳务需求列表发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "查询劳务需求列表失败，系统异常");
        }
    }
    
    /**
     * 获取项目的所有劳务需求
     * @param projectId 项目ID
     * @return 劳务需求列表
     */
    @GetMapping("/project/{projectId}")
    public BaseResponse<List<LaborDemandVO>> getLaborDemandsByProject(@PathVariable("projectId") Integer projectId) {
        try {
            // 调用服务层获取项目的劳务需求列表
            List<LaborDemandVO> laborDemandList = laborDemandService.getLaborDemandsByProjectId(projectId);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandList);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取项目劳务需求列表失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取项目劳务需求列表发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取项目劳务需求列表失败，系统异常");
        }
    }
    
    /**
     * 获取项目的所有劳务需求（更符合原设计的URL）
     * @param projectId 项目ID
     * @return 劳务需求列表
     */
    @GetMapping("/by-project/{projectId}")
    public BaseResponse<List<LaborDemandVO>> getLaborDemandsByProjectAlias(@PathVariable("projectId") Integer projectId) {
        return getLaborDemandsByProject(projectId);
    }
    
    /**
     * 检查用户是否有权限对劳务需求进行操作
     * @param id 劳务需求ID
     * @return 是否有操作权限
     */
    @GetMapping("/check-permission/{id}")
    public BaseResponse<Boolean> checkOperationPermission(@PathVariable("id") Integer id) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层检查权限
            Boolean hasPermission = laborDemandService.checkOperationPermission(id, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(hasPermission);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("检查操作权限失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("检查操作权限失败发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "检查操作权限失败，系统异常");
        }
    }
    
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
    @GetMapping("/search")
    public BaseResponse<Page<LaborDemandVO>> searchLaborDemands(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "minDailyWage", required = false) Double minDailyWage,
            @RequestParam(value = "maxDailyWage", required = false) Double maxDailyWage,
            @RequestParam(value = "startDateFrom", required = false) String startDateFrom,
            @RequestParam(value = "startDateTo", required = false) String startDateTo,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            // 调用服务层搜索劳务需求
            Page<LaborDemandVO> laborDemandPage = laborDemandService.searchLaborDemands(
                    keyword, province, city, district, minDailyWage, maxDailyWage,
                    startDateFrom, startDateTo, page, size);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandPage);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("搜索劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("搜索劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "搜索劳务需求失败，系统异常");
        }
    }
    
    /**
     * 获取推荐的劳务需求
     * @param limit 限制数量
     * @return 推荐的劳务需求列表
     */
    @GetMapping("/recommended")
    public BaseResponse<List<LaborDemandVO>> getRecommendedLaborDemands(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        try {
            // 调用服务层获取推荐劳务需求
            List<LaborDemandVO> laborDemandList = laborDemandService.getRecommendedLaborDemands(limit);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandList);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取推荐劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取推荐劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取推荐劳务需求失败，系统异常");
        }
    }
    
    /**
     * 获取特定项目的劳务需求统计信息
     * @param projectId 项目ID
     * @return 项目需求统计信息
     */
    @GetMapping("/stats/project/{projectId}")
    public BaseResponse<Map<String, Object>> getProjectDemandStats(@PathVariable("projectId") Integer projectId) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层获取项目需求统计信息
            Map<String, Object> statsData = laborDemandService.getProjectDemandStats(projectId);
            
            // 返回成功响应
            return ResultUtils.success(statsData);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取项目需求统计信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取项目需求统计信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取项目需求统计信息失败，系统异常");
        }
    }
    
    /**
     * 获取特定公司的劳务需求统计信息
     * @param companyId 公司ID
     * @return 公司需求统计信息
     */
    @GetMapping("/stats/company/{companyId}")
    public BaseResponse<Map<String, Object>> getCompanyDemandStats(@PathVariable("companyId") Integer companyId) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层获取公司需求统计信息
            Map<String, Object> statsData = laborDemandService.getCompanyDemandStats(companyId);
            
            // 返回成功响应
            return ResultUtils.success(statsData);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取公司需求统计信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取公司需求统计信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取公司需求统计信息失败，系统异常");
        }
    }
    
    /**
     * 分析工种需求热度
     * @param period 统计周期（天）
     * @return 工种需求热度分析数据
     */
    @GetMapping("/stats/occupation-heat")
    public BaseResponse<List<Map<String, Object>>> analyzeOccupationHeat(
            @RequestParam(value = "period", defaultValue = "30") Integer period) {
        try {
            // 调用服务层分析工种需求热度
            List<Map<String, Object>> heatData = laborDemandService.analyzeOccupationHeat(period);
            
            // 返回成功响应
            return ResultUtils.success(heatData);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("分析工种需求热度失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("分析工种需求热度发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "分析工种需求热度失败，系统异常");
        }
    }
    
    /**
     * 根据工种ID获取劳务需求
     * @param occupationId 工种ID
     * @param status 需求状态
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    @GetMapping("/by-occupation/{occupationId}")
    public BaseResponse<Page<LaborDemandVO>> getLaborDemandsByOccupation(
            @PathVariable("occupationId") Integer occupationId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            // 调用服务层获取指定工种的劳务需求
            Page<LaborDemandVO> laborDemandPage = laborDemandService.getLaborDemandsByOccupation(
                    occupationId, status, page, size);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandPage);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取工种劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取工种劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取工种劳务需求失败，系统异常");
        }
    }
    
    /**
     * 根据项目特征和需求描述推荐合适的工种
     * @param projectFeatures 项目特征
     * @return 推荐的工种列表及匹配度
     */
    @PostMapping("/suggest-occupations")
    public BaseResponse<List<Map<String, Object>>> suggestOccupations(@RequestBody Map<String, Object> projectFeatures) {
        try {
            // 从请求体中提取需求描述
            String demandDescription = (String) projectFeatures.getOrDefault("demandDescription", "");
            
            // 调用服务层推荐工种
            List<Map<String, Object>> occupationsList = laborDemandService.suggestOccupations(
                    projectFeatures, demandDescription);
            
            // 返回成功响应
            return ResultUtils.success(occupationsList);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("推荐合适工种失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("推荐合适工种发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "推荐合适工种失败，系统异常");
        }
    }
    
    /**
     * 根据地区获取劳务需求
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    @GetMapping("/by-location")
    public BaseResponse<Page<LaborDemandVO>> getLaborDemandsByLocation(
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            // 调用服务层获取指定地区的劳务需求
            Page<LaborDemandVO> laborDemandPage = laborDemandService.getLaborDemandsByLocation(
                    province, city, district, page, size);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandPage);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取地区劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取地区劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取地区劳务需求失败，系统异常");
        }
    }
    
    /**
     * 获取劳务需求最多的热门地区
     * @param limit 限制数量
     * @return 热门地区信息列表
     */
    @GetMapping("/hot-locations")
    public BaseResponse<List<Map<String, Object>>> getHotDemandLocations(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        try {
            // 调用服务层获取热门需求地区
            List<Map<String, Object>> hotLocations = laborDemandService.getHotDemandLocations(limit);
            
            // 返回成功响应
            return ResultUtils.success(hotLocations);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取热门需求地区失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取热门需求地区发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取热门需求地区失败，系统异常");
        }
    }
    
    /**
     * 根据项目类型、工种和地区综合查询劳务需求
     * @param projectType 项目类型ID
     * @param occupationId 工种ID
     * @param location 地区（省/市/区）
     * @param page 页码
     * @param size 每页大小
     * @return 劳务需求分页列表
     */
    @GetMapping("/criteria-query")
    public BaseResponse<Page<LaborDemandVO>> queryLaborDemands(
            @RequestParam(value = "projectType", required = false) Integer projectType,
            @RequestParam(value = "occupationId", required = false) Integer occupationId,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            // 调用服务层查询劳务需求
            Page<LaborDemandVO> laborDemandPage = laborDemandService.queryLaborDemandsByCriteria(
                    projectType, occupationId, location, page, size);
            
            // 返回成功响应
            return ResultUtils.success(laborDemandPage);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("查询劳务需求失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("查询劳务需求发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "查询劳务需求失败，系统异常");
        }
    }
}
