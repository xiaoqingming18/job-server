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

import java.util.List;

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
            log.error("检查操作权限发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "检查操作权限失败，系统异常");
        }
    }
}
