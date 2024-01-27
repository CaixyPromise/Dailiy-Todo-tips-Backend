package com.caixy.adminSystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务分类组的返回体
 *
 * @name: com.caixy.adminSystem.model.vo.TaskDirectoriesVO
 * @author: CAIXYPROMISE
 * @since: 2024-01-24 00:56
 **/
@Data
public class TaskDirectoriesVO implements Serializable
{
    private Long id;
    private String name;
    private static final long serialVersionUID = 1L;
}
