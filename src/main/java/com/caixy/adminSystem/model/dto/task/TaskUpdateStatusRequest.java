package com.caixy.adminSystem.model.dto.task;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务状态更新请求
 *
 * @name: com.caixy.adminSystem.model.dto.task.TaskUpdateStatusRequest
 * @author: CAIXYPROMISE
 * @since: 2024-01-23 23:56
 **/
@Data
public class TaskUpdateStatusRequest implements Serializable
{
    private Long taskId;
    private Integer status;
    private static final long serialVersionUID = 1L;
}
