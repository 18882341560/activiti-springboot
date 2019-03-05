package com.gelin.activitispringboot.actviti.exclusive;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ExclusiveExecutionListener implements ExecutionListener {



    @Override
    public void notify(DelegateExecution delegateExecution) {
        String eventName = delegateExecution.getEventName();
        if(Objects.equals(eventName,"start")){
            System.out.println("流程开始");
        }

        if(Objects.equals(eventName,"end")){
            System.out.println("流程结束");
        }
    }


}
