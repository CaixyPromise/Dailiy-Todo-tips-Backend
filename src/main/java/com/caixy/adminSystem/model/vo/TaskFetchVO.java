package com.caixy.adminSystem.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 获取任务数据返回结构
 *
 * @name: com.caixy.adminSystem.model.vo.TaskFetchVO
 * @author: CAIXYPROMISE
 * @since: 2024-01-24 01:30
 **/
@Data
public class TaskFetchVO implements Serializable
{
    List<TaskVO> tasks;
    List<TaskDirectoriesVO> directories;
    private static final long serialVersionUID = 1L;
}
