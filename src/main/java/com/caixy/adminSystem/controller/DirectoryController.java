package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.TasksMapper;
import com.caixy.adminSystem.model.dto.Directories.DirectoryAddRequest;
import com.caixy.adminSystem.model.dto.Directories.DirectoryUpdateRequest;
import com.caixy.adminSystem.model.entity.TaskDirectories;
import com.caixy.adminSystem.model.entity.Tasks;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.service.TaskDirectoriesService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 目录分组控制器
 *
 * @name: com.caixy.adminSystem.controller.DirectoryController
 * @author: CAIXYPROMISE
 * @since: 2024-01-25 18:36
 **/
@Slf4j
@RestController
@RequestMapping("/directories")
public class DirectoryController
{
    @Resource
    private TaskDirectoriesService taskDirectoriesService;

    @Resource
    private TasksMapper tasksMapper;

    @Resource
    private UserService userService;

    /**
     * 创建目录分组
     *
     * @param directoriesItem
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTaskDirectories(@RequestBody DirectoryAddRequest directoriesItem, HttpServletRequest request)
    {
        if (directoriesItem == null || directoriesItem.getTagName().isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        TaskDirectories newDirectoriesItem = new TaskDirectories();
        // 创建新的目录分组
        newDirectoriesItem.setUserId(loginUser.getId());
        newDirectoriesItem.setTagName(directoriesItem.getTagName().trim());
        // 校验
        taskDirectoriesService.validDirectories(newDirectoriesItem, true);
        boolean result = taskDirectoriesService.save(newDirectoriesItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newDirectoriesId = newDirectoriesItem.getId();
        return ResultUtils.success(newDirectoriesId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTasks(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TaskDirectories oldTasks = taskDirectoriesService.getById(id);
        ThrowUtils.throwIf(oldTasks == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        ThrowUtils.throwIf(!oldTasks.getUserId().equals(user.getId()) && !userService.isAdmin(request),
                ErrorCode.NO_AUTH_ERROR, "无操作权限");
        boolean b = taskDirectoriesService.removeById(id);
        //  删除目录下的任务
        tasksMapper.delete(new LambdaQueryWrapper<Tasks>().eq(Tasks::getDirId, id));
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param updateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTasks(@RequestBody DirectoryUpdateRequest updateRequest,
                                             HttpServletRequest request)
    {
        if (updateRequest == null || updateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = updateRequest.getId();
        // 判断是否存在
        TaskDirectories oldTasks = taskDirectoriesService.getById(id);
        // 校验权限，只有自己才能修改
        ThrowUtils.throwIf(oldTasks == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!oldTasks.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR);
        // 生成一份新的目录结构体
        TaskDirectories newTaskDirectories = new TaskDirectories();
        BeanUtils.copyProperties(updateRequest, newTaskDirectories);
        newTaskDirectories.setUserId(loginUser.getId());
        // 参数校验
        taskDirectoriesService.validDirectories(newTaskDirectories, false);

        boolean result = taskDirectoriesService.updateById(newTaskDirectories);
        return ResultUtils.success(result);
    }
}
