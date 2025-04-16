package com.qingming.jobserver.model.dao.occupation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新工种类别请求
 */
@Data
public class CategoryUpdateRequest {
    
    /**
     * 类别ID
     */
    @NotNull(message = "类别ID不能为空")
    private Integer id;
    
    /**
     * 类别名称
     */
    @Size(max = 50, message = "类别名称长度不能超过50个字符")
    private String name;
    
    /**
     * 类别图标
     */
    private String icon;
    
    /**
     * 类别描述
     */
    private String description;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
} 