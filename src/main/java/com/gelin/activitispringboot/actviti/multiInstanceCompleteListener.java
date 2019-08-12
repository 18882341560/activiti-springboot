package com.gelin.activitispringboot.actviti;

import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Objects;


/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/8/5
 * @description:
 */
public class multiInstanceCompleteListener implements TaskListener {
    private static final long serialVersionUID = -642708315294921408L;

    private RuntimeService runtimeService;
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        if (runtimeService == null) {
            runtimeService = (RuntimeService) SpringContextUtils.getBeanByClass(RuntimeService.class);
        }
        if (taskService == null) {
            taskService = (TaskService) SpringContextUtils.getBeanByClass(TaskService.class);
        }
        Integer check = delegateTask.getVariable("check", Integer.class);
        String executionId = delegateTask.getExecutionId();
        // 回退
        if (Objects.equals(check, 1)) {
            runtimeService.setVariable(executionId, "check", 1);
        } else {
            Integer nrOfInstances = runtimeService.getVariable(executionId, "nrOfInstances", Integer.class);
            Integer nrOfCompletedInstances = runtimeService.getVariable(executionId, "nrOfCompletedInstances", Integer.class);
            System.out.println(nrOfInstances);
            System.out.println(nrOfCompletedInstances);
            int completeRate = nrOfCompletedInstances / nrOfInstances;
            if (Objects.equals(completeRate, 1)) {
                runtimeService.setVariable(executionId, "check", 2);
            }
        }
    }
}
