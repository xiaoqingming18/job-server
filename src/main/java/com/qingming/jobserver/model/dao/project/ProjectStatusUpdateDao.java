package com.qingming.jobserver.model.dao.project;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 更新项目状态接口入参数据对象
 */
@Data
public class ProjectStatusUpdateDao {

    /**
     * 项目ID（必填）
     */
    @NotNull(message = "项目ID不能为空")
    private Integer id;

    /**
     * 项目状态（必填）
     * 待开工(pending)/进行中(in_progress)/已完成(completed)/已暂停(paused)/已取消(cancelled)
     */
    @NotBlank(message = "项目状态不能为空")
    @Pattern(regexp = "^(pending|in_progress|completed|paused|cancelled)$", 
            message = "项目状态必须为：pending、in_progress、completed、paused或cancelled")
    private String status;
}