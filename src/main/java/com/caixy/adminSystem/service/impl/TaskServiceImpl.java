package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.TaskDirectoriesMapper;
import com.caixy.adminSystem.mapper.TasksMapper;
import com.caixy.adminSystem.model.entity.TaskDirectories;
import com.caixy.adminSystem.model.entity.Tasks;
import com.caixy.adminSystem.model.vo.TaskDirectoriesVO;
import com.caixy.adminSystem.model.vo.TaskFetchVO;
import com.caixy.adminSystem.model.vo.TaskVO;
import com.caixy.adminSystem.service.TaskService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【tasks】的数据库操作Service实现
 * @createDate 2024-01-21 01:42:57
 */
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TasksMapper, Tasks>
        implements TaskService
{
    @Resource
    private UserService userService;

    @Resource
    private TaskDirectoriesMapper taskDirectoriesMapper;

    private final int MAX_TITLE_LENGTH = 255;
    private final int MAX_CONTENT_LENGTH = 1024;


    @Override
    public void validTask(Tasks item, boolean add)
    {
        if (item == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = item.getTitle();
        String content = item.getDescription();
        log.info("item: {}", item);
        // 原有目录必需要存在
        Long tags = taskDirectoriesMapper.selectCount(Wrappers.<TaskDirectories>lambdaQuery().in(TaskDirectories::getId,
                        item.getDirId()));
        // 校验结果是否存在
        ThrowUtils.throwIf(tags == null || tags <= 0, ErrorCode.NOT_FOUND_ERROR, "目录不存在");
        // 创建时，参数不能为空
        ThrowUtils.throwIf(add && StringUtils.isAnyBlank(title, content), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(title.length() > MAX_TITLE_LENGTH, ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(content.length() > MAX_CONTENT_LENGTH, ErrorCode.PARAMS_ERROR, "内容过长");
    }

    @Override
    public TaskVO getTaskVO(Tasks taskItem)
    {
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(taskItem, taskVO);
        taskVO.setDir(taskItem.getDirId());
        taskVO.setAlarm(taskItem.getAlarm() == 1);
        taskVO.setCompleted(taskItem.getCompleted() == 1);
        taskVO.setImportant(taskItem.getImportant() == 1);
        return taskVO;
    }

    public TaskDirectoriesVO getDirectoryVO(TaskDirectories directoryItem)
    {
        TaskDirectoriesVO directoryVO = new TaskDirectoriesVO();
        directoryVO.setId(directoryItem.getId());
        directoryVO.setName(directoryItem.getTagName());
        log.info("directoryItem: {}, directoryVO: {}", directoryItem, directoryVO);
        return directoryVO;    // 查询当前用户的任务和任务分类
    }


    @Override
    public TaskFetchVO fetchTasks(Long userId, boolean isImportant)
    {
        TaskFetchVO fetchVO = new TaskFetchVO();
        // 查任务
        QueryWrapper<Tasks> queryTaskWrapper = new QueryWrapper<>();
        queryTaskWrapper.eq("userID", userId);
        if (isImportant)
        {
            queryTaskWrapper.eq("important", 1);
        }
        // 查用户的目录分组
        QueryWrapper<TaskDirectories> queryDirectoriesWrapper = new QueryWrapper<>();
        queryDirectoriesWrapper.eq("user_id", userId);
        List<Tasks> tasksList = this.list(queryTaskWrapper);
        List<TaskDirectories> taskDirectoriesList = this.taskDirectoriesMapper.selectList(queryDirectoriesWrapper);
        log.info("tasksList: {}, taskDirectoriesList: {}", tasksList, taskDirectoriesList);
        // 合并封装
        // 封装任务
        fetchVO.setTasks(tasksList.stream().map(this::getTaskVO).collect(Collectors.toList()));
        // 封装分组目录
        fetchVO.setDirectories(taskDirectoriesList.stream().map(this::getDirectoryVO).collect(Collectors.toList()));
        return fetchVO;
    }
}




