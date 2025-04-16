package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.occupation.CategoryAddRequest;
import com.qingming.jobserver.model.dao.occupation.CategoryUpdateRequest;
import com.qingming.jobserver.model.entity.OccupationCategory;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.service.OccupationCategoryService;
import com.qingming.jobserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 工种类别管理控制器
 */
@RestController
@RequestMapping("/occupation/category")
public class OccupationCategoryController {

    @Autowired
    private OccupationCategoryService categoryService;
    
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
     * 添加工种类别（仅系统管理员可操作）
     * @param categoryAddRequest 添加工种类别请求
     * @return 添加后的工种类别对象
     */
    @PostMapping("/add")
    public BaseResponse<OccupationCategory> addCategory(@RequestBody @Valid CategoryAddRequest categoryAddRequest) {
        // 检查权限
        checkSystemAdminRole();
        
        OccupationCategory category = categoryService.addCategory(categoryAddRequest);
        return ResultUtils.success(category);
    }

    /**
     * 更新工种类别信息（仅系统管理员可操作）
     * @param categoryUpdateRequest 更新工种类别请求
     * @return 更新后的工种类别对象
     */
    @PutMapping("/update")
    public BaseResponse<OccupationCategory> updateCategory(@RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        // 检查权限
        checkSystemAdminRole();
        
        OccupationCategory category = categoryService.updateCategory(categoryUpdateRequest);
        return ResultUtils.success(category);
    }

    /**
     * 根据ID获取工种类别详情（所有已认证用户可访问）
     * @param id 工种类别ID
     * @return 工种类别对象
     */
    @GetMapping("/info/{id}")
    public BaseResponse<OccupationCategory> getCategoryById(@PathVariable Integer id) {
        OccupationCategory category = categoryService.getCategoryById(id);
        return ResultUtils.success(category);
    }

    /**
     * 获取所有工种类别列表（所有已认证用户可访问）
     * @return 工种类别列表
     */
    @GetMapping("/list")
    public BaseResponse<List<OccupationCategory>> listAllCategories() {
        List<OccupationCategory> categories = categoryService.listAllCategories();
        return ResultUtils.success(categories);
    }

    /**
     * 删除工种类别（仅系统管理员可操作）
     * @param id 工种类别ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<String> deleteCategory(@PathVariable Integer id) {
        // 检查权限
        checkSystemAdminRole();
        
        boolean result = categoryService.deleteCategory(id);
        if (result) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(50001, "删除失败");
        }
    }
} 