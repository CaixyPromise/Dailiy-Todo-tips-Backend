package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tasks
 */
@TableName(value = "tasks")
@Data
public class Tasks implements Serializable
{
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务所属目录
     */
    private Long dirId;

    /**
     * 任务所属用户
     */
    private Long userid;

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
    private Integer completed;

    /**
     * 任务是否重要
     */
    private Integer important;

    /**
     * 任务是否有提醒
     */
    private Integer alarm;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}