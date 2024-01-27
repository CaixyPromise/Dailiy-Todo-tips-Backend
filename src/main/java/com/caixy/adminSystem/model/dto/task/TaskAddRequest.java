package com.caixy.adminSystem.model.dto.task;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class TaskAddRequest implements Serializable
{

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务所属目录
     */
    private Long dir;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务日期
     */
    private Date date;

    /**
     * 任务是否完成
     */
    private Boolean completed;

    /**
     * 任务是否重要
     */
    private Boolean important;

    /**
     * 任务是否有提醒
     */
    private Boolean alarm;

    private static final long serialVersionUID = 1L;
}