package com.caixy.adminSystem.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tasks
 */
@Data
public class TaskVO implements Serializable
{
    /**
     * 任务id
     */
    private Long id;
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