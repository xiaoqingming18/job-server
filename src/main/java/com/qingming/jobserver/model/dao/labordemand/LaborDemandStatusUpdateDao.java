package com.qingming.jobserver.model.dao.labordemand;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新劳务需求状态请求参数
 */
@Data
public class LaborDemandStatusUpdateDao {
    
    /**
     * 需求ID
     */
    @NotNull(message = "需求ID不能为空")
    private Integer id;
    
    /**
     * 需求状态（open-开放中、filled-已满、cancelled-已取消、expired-已过期）
     */
    @NotNull(message = "需求状态不能为空")
    private String status;
}
