package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.TaskDirectoriesMapper;
import com.caixy.adminSystem.model.entity.TaskDirectories;
import com.caixy.adminSystem.service.TaskDirectoriesService;
import org.springframework.stereotype.Service;

/**
 * @author CAIXYPROMISE
 * @description 针对表【task_directories(任务分类标签表)】的数据库操作Service实现
 * @createDate 2024-01-24 00:50:16
 */
@Service
public class TaskDirectoriesServiceImpl extends ServiceImpl<TaskDirectoriesMapper, TaskDirectories>
        implements TaskDirectoriesService
{
    @Override
    public void validDirectories(TaskDirectories directoriesItem, boolean add)
    {
        boolean isLengthValid = directoriesItem.getTagName().length() <= 32;
        boolean isExistingInDb = !add && this.count(new QueryWrapper<TaskDirectories>()
                .eq("user_id", directoriesItem.getUserId())
                .eq("tagName", directoriesItem.getTagName())) > 0;
        boolean result = isLengthValid || isExistingInDb;
        ThrowUtils.throwIf(!result, ErrorCode.PARAMS_ERROR, "目录分组名称过长或已存在");
    }
}




