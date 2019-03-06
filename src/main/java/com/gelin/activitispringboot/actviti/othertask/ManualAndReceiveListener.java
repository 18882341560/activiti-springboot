package com.gelin.activitispringboot.actviti.othertask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;


/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/6/006
 * @description:
 */
public class ManualAndReceiveListener implements ExecutionListener {


    private static final long serialVersionUID = -6354951643794203648L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String phone = (String)delegateExecution.getVariable("phone");
        System.out.println("给这个电话号码:"+phone+"发送短信");
    }
}
