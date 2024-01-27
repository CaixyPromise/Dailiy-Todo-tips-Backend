package com.caixy.adminSystem.model.dto.task;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class TaskUpdateRequest implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 任务结构
     */
    private TaskAddRequest task;

    private static final long serialVersionUID = 1L;
}