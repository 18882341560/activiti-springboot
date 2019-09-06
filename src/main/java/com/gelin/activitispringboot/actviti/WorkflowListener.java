package com.gelin.activitispringboot.actviti;


import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.gelin.activitispringboot.constant.SystemProcessConst.*;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/4/18/018
 * @description: 设置所有任务创建时的监听
 */
@Service
public class WorkflowListener implements TaskListener {
    private static final long serialVersionUID = 5952571575432248961L;

    private TaskService taskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(DelegateTask delegateTask) {
        if (taskService == null) {
            taskService = (TaskService) SpringContextUtils.getBeanByClass(TaskService.class);
        }
        //获取所有的传入的变量
        Map<String, Object> variables = delegateTask.getVariables();
        //获取代办人,如果没有就设置为当前登陆的用户
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) variables.get(CANDIDATE_KEY);
        delegateTask.setVariable(CANDIDATE_KEY, list);
    }
}
