package com.gelin.activitispringboot;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * create gl  2018/12/22
 **/
public class ExclusiveGateWayLister implements ExecutionListener {

    private static final long serialVersionUID = 1605010742126954132L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("监听");
        System.out.println(delegateExecution.getId());
        System.out.println(delegateExecution.getProcessDefinitionId());
        System.out.println(delegateExecution.getProcessInstanceId());
        System.out.println(delegateExecution.getCurrentActivitiListener());
        System.out.println(delegateExecution.getCurrentActivityId());
        System.out.println(delegateExecution.getCurrentFlowElement());
        System.out.println(delegateExecution.getEventName());
        System.out.println(delegateExecution.getExecutions());
        System.out.println(delegateExecution.getParent());
        System.out.println(delegateExecution.getParentId());
        System.out.println(delegateExecution.getProcessInstanceBusinessKey());
        System.out.println(delegateExecution.getRootProcessInstanceId());
    }
}
