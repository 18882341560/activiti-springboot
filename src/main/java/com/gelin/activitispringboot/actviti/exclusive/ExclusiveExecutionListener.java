package com.gelin.activitispringboot.actviti.exclusive;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ExclusiveExecutionListener implements ExecutionListener {



    @Override
    public void notify(DelegateExecution delegateExecution) {
        String eventName = delegateExecution.getEventName();
        System.out.println("eventName:"+eventName);
    }


}
