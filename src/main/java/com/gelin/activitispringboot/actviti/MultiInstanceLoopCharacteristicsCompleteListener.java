package com.gelin.activitispringboot.actviti;

import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.gelin.activitispringboot.constant.ActivitiDefaultVariable.*;
import static com.gelin.activitispringboot.constant.SystemProcessConst.*;


/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/8/16
 * @description:
 */
@Service
public class MultiInstanceLoopCharacteristicsCompleteListener implements TaskListener {

    private static final long serialVersionUID = -5418118501757020034L;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(DelegateTask delegateTask) {
        if (runtimeService == null) {
            runtimeService = (RuntimeService) SpringContextUtils.getBeanByClass(RuntimeService.class);
        }
        if (taskService == null) {
            taskService = (TaskService) SpringContextUtils.getBeanByClass(TaskService.class);
        }
        Integer check = delegateTask.getVariable(CHECK_KEY, Integer.class);
        String executionId = delegateTask.getExecutionId();
        // 回退
        if (Objects.equals(check, ROLL_BACK)) {
            runtimeService.setVariable(executionId, CHECK_KEY, ROLL_BACK);
        } else {
            // 会签中的实例个数
            Integer nrOfInstances = runtimeService.getVariable(executionId, NR_OF_INSTANCES, Integer.class);
            // 完成的实例个数
            Integer nrOfCompletedInstances = runtimeService.getVariable(executionId, NR_OF_COMPLETED_INSTANCES, Integer.class);
            int completeRate = nrOfCompletedInstances / nrOfInstances;
            // 如果所有的都通过完成
            if (Objects.equals(completeRate, 1)) {
                runtimeService.setVariable(executionId, CHECK_KEY, PASS);
            }
        }
    }
}
