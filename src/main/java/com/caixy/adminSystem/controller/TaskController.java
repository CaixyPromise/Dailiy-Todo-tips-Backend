package com.caixy.adminSystem.controller;

import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.task.TaskAddRequest;
import com.caixy.adminSystem.model.dto.task.TaskUpdateRequest;
import com.caixy.adminSystem.model.dto.task.TaskUpdateStatusRequest;
import com.caixy.adminSystem.model.entity.Tasks;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.TaskFetchVO;
import com.caixy.adminSystem.model.vo.TaskVO;
import com.caixy.adminSystem.service.TaskService;
import com.caixy.adminSystem.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 */
@ApiOperation(value = "任务管理接口")
@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController
{

    @Resource
    private TaskService taskService;

    @Resource
    private UserService userService;


    // region 更新字段常量
    private final static int UPDATE_COMPLETED = 0;
    private final static int UPDATE_IMPORTANT = 1;
    private final static int UPDATE_ALARM = 2;
    // endregion

    // region 任务 增删改查

    /**
     * 创建
     *
     * @param taskAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTasks(@RequestBody TaskAddRequest taskAddRequest, HttpServletRequest request)
    {
        if (taskAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Tasks taskItem = new Tasks();
        BeanUtils.copyProperties(taskAddRequest, taskItem);
        taskItem.setDirId(taskAddRequest.getDir());
        //  校验
        taskService.validTask(taskItem, true);
        // 保存任务
        taskItem.setUserid(loginUser.getId());
        boolean result = taskService.save(taskItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTasksId = taskItem.getId();
        return ResultUtils.success(newTasksId);
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
        Tasks oldTasks = taskService.getById(id);
        ThrowUtils.throwIf(oldTasks == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        ThrowUtils.throwIf(!oldTasks.getUserid().equals(user.getId()) && !userService.isAdmin(request),
                ErrorCode.NO_AUTH_ERROR, "无操作权限");
        boolean b = taskService.removeById(id);

        return ResultUtils.success(b);
    }

    /**
     * 更新任务状态信息
     * <p>* status: 0 完成状态</p>
     * <p>* status: 1 重要状态</p>
     * <p>* status: 2 提醒状态</p>
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/1/24 19:15
     */
    @PostMapping("/update/status")
    public BaseResponse<Boolean> updateStatus(@RequestBody TaskUpdateStatusRequest statusRequest, HttpServletRequest request)
    {
        if (statusRequest == null || statusRequest.getTaskId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = statusRequest.getTaskId();
        // 判断是否存在
        Tasks oldTasks = taskService.getById(id);
        ThrowUtils.throwIf(oldTasks == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人可操作
        ThrowUtils.throwIf(!oldTasks.getUserid().equals(user.getId()),
                ErrorCode.NO_AUTH_ERROR, "无操作权限");
        Tasks newTaskBody = new Tasks();
        BeanUtils.copyProperties(oldTasks, newTaskBody);
        switch (statusRequest.getStatus())
        {
        case UPDATE_COMPLETED: // 更新完成状态
            newTaskBody.setCompleted(getBoolean(newTaskBody.getCompleted()));
            break;
        case UPDATE_IMPORTANT: // 更新事件重要状态
            newTaskBody.setImportant(getBoolean(newTaskBody.getImportant()));
            break;
        case UPDATE_ALARM: // 更新提醒状态
            newTaskBody.setAlarm(getBoolean(newTaskBody.getAlarm()));
            break;
        default:
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean update = taskService.updateById(newTaskBody);
        return ResultUtils.success(update);
    }

    /**
     * 更新
     *
     * @param taskUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTasks(@RequestBody TaskUpdateRequest taskUpdateRequest)
    {
        if (taskUpdateRequest == null || taskUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tasks post = new Tasks();
        BeanUtils.copyProperties(taskUpdateRequest.getTask(), post);
        post.setDirId(taskUpdateRequest.getTask().getDir());
        post.setId(taskUpdateRequest.getId());
//        List<String> tags = postUpdateRequest.getTags();
//        if (tags != null)
//        {
//            post.setTags(JSONUtil.toJsonStr(tags));
//        }
        // 参数校验
        taskService.validTask(post, false);
        long id = taskUpdateRequest.getId();
        // 判断是否存在
        Tasks oldTasks = taskService.getById(id);
        ThrowUtils.throwIf(oldTasks == null, ErrorCode.NOT_FOUND_ERROR);
        log.info("post: {}", post);
        boolean result = taskService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TaskVO> getTasksVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tasks tasks = taskService.getById(id);
        if (tasks == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(taskService.getTaskVO(tasks));
    }

    /**
     * 获取用户的任务列表
     *
     * @return 任务清单json(所有任务)
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/1/22 0:26
     */
    @GetMapping("/fetch")
    public BaseResponse<TaskFetchVO> fetchTasks(HttpServletRequest request)
    {
        return ResultUtils.success(taskService.fetchTasks(userService.getLoginUser(request).getId(), false));
    }


    private Integer getBoolean(@NotNull Integer value)
    {
        return value == 0 ? 1 : 0;
    }

}
