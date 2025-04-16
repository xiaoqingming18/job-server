package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.occupation.OccupationAddRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationStatusUpdateRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationUpdateRequest;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.service.OccupationService;
import com.qingming.jobserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工种管理控制器
 */
@RestController
@RequestMapping("/occupation")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 检查当前用户是否为系统管理员
     * 如果不是系统管理员则抛出无权限异常
     */
    private void checkSystemAdminRole() {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        User user = userService.getById(currentUserId);
        if (user == null || user.getRole() != UserRoleEnum.system_admin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只有系统管理员可以执行此操作");
        }
    }

    /**
     * 添加工种（仅系统管理员可操作）
     * @param occupationAddRequest 添加工种请求
     * @return 添加后的工种对象
     */
    @PostMapping("/add")
    public BaseResponse<Occupation> addOccupation(@RequestBody @Valid OccupationAddRequest occupationAddRequest) {
        // 检查权限
        checkSystemAdminRole();
        
        Occupation occupation = occupationService.addOccupation(occupationAddRequest);
        return ResultUtils.success(occupation);
    }

    /**
     * 更新工种信息（仅系统管理员可操作）
     * @param occupationUpdateRequest 更新工种请求
     * @return 更新后的工种对象
     */
    @PutMapping("/update")
    public BaseResponse<Occupation> updateOccupation(@RequestBody @Valid OccupationUpdateRequest occupationUpdateRequest) {
        // 检查权限
        checkSystemAdminRole();
        
        Occupation occupation = occupationService.updateOccupation(occupationUpdateRequest);
        return ResultUtils.success(occupation);
    }

    /**
     * 更新工种状态（启用/禁用）（仅系统管理员可操作）
     * @param statusUpdateRequest 状态更新请求
     * @return 更新后的工种对象
     */
    @PutMapping("/status")
    public BaseResponse<Occupation> updateOccupationStatus(@RequestBody @Valid OccupationStatusUpdateRequest statusUpdateRequest) {
        // 检查权限
        checkSystemAdminRole();
        
        Occupation occupation = occupationService.updateOccupationStatus(statusUpdateRequest);
        return ResultUtils.success(occupation);
    }

    /**
     * 根据ID获取工种详情（所有已认证用户可访问）
     * @param id 工种ID
     * @return 工种对象
     */
    @GetMapping("/info/{id}")
    public BaseResponse<Occupation> getOccupationById(@PathVariable Integer id) {
        Occupation occupation = occupationService.getOccupationById(id);
        return ResultUtils.success(occupation);
    }

    /**
     * 分页获取工种列表（所有已认证用户可访问）
     * @param page 页码，从1开始
     * @param size 每页数量
     * @param categoryId 工种类别ID（可选）
     * @return 工种列表和分页信息
     */
    @GetMapping("/page")
    public BaseResponse<Map<String, Object>> pageOccupations(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer categoryId) {
        
        Map<String, Object> pageResult = occupationService.pageOccupations(page, size, categoryId);
        return ResultUtils.success(pageResult);
    }

    /**
     * 根据类别ID获取工种列表（所有已认证用户可访问）
     * @param categoryId 类别ID
     * @return 工种列表
     */
    @GetMapping("/by-category/{categoryId}")
    public BaseResponse<List<Occupation>> listOccupationsByCategoryId(@PathVariable Integer categoryId) {
        List<Occupation> occupations = occupationService.listOccupationsByCategoryId(categoryId);
        return ResultUtils.success(occupations);
    }

    /**
     * 根据平均日薪范围查询工种（所有已认证用户可访问）
     * @param minWage 最低日薪（可选）
     * @param maxWage 最高日薪（可选）
     * @return 符合条件的工种列表
     */
    @GetMapping("/by-wage")
    public BaseResponse<List<Occupation>> listOccupationsByWage(
            @RequestParam(required = false) BigDecimal minWage,
            @RequestParam(required = false) BigDecimal maxWage) {
        
        List<Occupation> occupations = occupationService.listOccupationsByWageRange(minWage, maxWage);
        return ResultUtils.success(occupations);
    }

    /**
     * 根据难度等级查询工种（所有已认证用户可访问）
     * @param level 难度等级(1-5)
     * @return 符合条件的工种列表
     */
    @GetMapping("/by-difficulty/{level}")
    public BaseResponse<List<Occupation>> listOccupationsByDifficulty(@PathVariable Integer level) {
        List<Occupation> occupations = occupationService.listOccupationsByDifficultyLevel(level);
        return ResultUtils.success(occupations);
    }

    /**
     * 工种搜索接口，支持多条件组合查询（所有已认证用户可访问）
     * @param name 工种名称关键词（可选）
     * @param categoryId 工种类别ID（可选）
     * @param minWage 最低日薪（可选）
     * @param maxWage 最高日薪（可选）
     * @param difficultyLevel 难度等级（可选）
     * @return 符合条件的工种列表
     */
    @GetMapping("/search")
    public BaseResponse<List<Occupation>> searchOccupations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minWage,
            @RequestParam(required = false) BigDecimal maxWage,
            @RequestParam(required = false) Integer difficultyLevel) {
        
        List<Occupation> occupations = occupationService.searchOccupations(name, categoryId, minWage, maxWage, difficultyLevel);
        return ResultUtils.success(occupations);
    }

    /**
     * 获取热门工种（按平均日薪排序，取前N个）（所有已认证用户可访问）
     * @param limit 返回数量，默认5个
     * @return 热门工种列表
     */
    @GetMapping("/hot")
    public BaseResponse<List<Occupation>> getHotOccupations(@RequestParam(defaultValue = "5") Integer limit) {
        List<Occupation> hotOccupations = occupationService.listHotOccupations(limit);
        return ResultUtils.success(hotOccupations);
    }

    /**
     * 删除工种（仅系统管理员可操作）
     * @param id 工种ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<String> deleteOccupation(@PathVariable Integer id) {
        // 检查权限
        checkSystemAdminRole();
        
        boolean result = occupationService.deleteOccupation(id);
        if (result) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(50001, "删除失败");
        }
    }
}