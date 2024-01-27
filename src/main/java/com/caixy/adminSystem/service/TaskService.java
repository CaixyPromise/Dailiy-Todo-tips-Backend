package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.Tasks;
import com.caixy.adminSystem.model.vo.TaskFetchVO;
import com.caixy.adminSystem.model.vo.TaskVO;

/**
 * @author CAIXYPROMISE
 * @description 针对表【tasks】的数据库操作Service
 * @createDate 2024-01-21 01:42:57
 */
public interface TaskService extends IService<Tasks>
{

    /**
     * 校验
     *
     * @param item
     * @param add
     */

    void validTask(Tasks item, boolean add);


    /**
     * 获取封装
     *
     * @param post
     * @return
     */
    TaskVO getTaskVO(Tasks post);

    /**
     * 获取当前用户的任务列表
     */
    TaskFetchVO fetchTasks(Long userId, boolean isImportant);
}
