package com.qingming.jobserver.model.dao.occupation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新工种状态请求
 */
@Data
public class OccupationStatusUpdateRequest {
    
    /**
     * 工种ID
     */
    @NotNull(message = "工种ID不能为空")
    private Integer id;
    
    /**
     * 状态：0-禁用，1-启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
} 