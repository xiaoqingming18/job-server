package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.occupation.CategoryAddRequest;
import com.qingming.jobserver.model.dao.occupation.CategoryUpdateRequest;
import com.qingming.jobserver.model.entity.OccupationCategory;

import java.util.List;

/**
 * 工种类别服务接口
 */
public interface OccupationCategoryService {

    /**
     * 添加工种类别
     * @param categoryAddRequest 添加工种类别请求
     * @return 添加后的工种类别
     */
    OccupationCategory addCategory(CategoryAddRequest categoryAddRequest);

    /**
     * 更新工种类别
     * @param categoryUpdateRequest 更新工种类别请求
     * @return 更新后的工种类别
     */
    OccupationCategory updateCategory(CategoryUpdateRequest categoryUpdateRequest);

    /**
     * 根据ID获取工种类别
     * @param id 工种类别ID
     * @return 工种类别对象
     */
    OccupationCategory getCategoryById(Integer id);

    /**
     * 获取所有工种类别
     * @return 工种类别列表
     */
    List<OccupationCategory> listAllCategories();

    /**
     * 删除工种类别
     * @param id 工种类别ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Integer id);

    /**
     * 根据名称获取工种类别
     * @param name 工种类别名称
     * @return 工种类别对象
     */
    OccupationCategory getCategoryByName(String name);
} 