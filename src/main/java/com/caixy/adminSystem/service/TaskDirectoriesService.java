package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.TaskDirectories;

/**
 * @author CAIXYPROMISE
 * @description 针对表【task_directories(任务分类标签表)】的数据库操作Service
 * @createDate 2024-01-24 00:50:16
 */
public interface TaskDirectoriesService extends IService<TaskDirectories>
{
    void validDirectories(TaskDirectories taskDirectories, boolean add);

}
