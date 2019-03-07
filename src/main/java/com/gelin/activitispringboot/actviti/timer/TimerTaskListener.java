package com.gelin.activitispringboot.actviti.timer;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: gl
 * @Email: 110.com
 * @version: 1.0
 * @Date: 2019/3/7
 * @describe:
 */
public class TimerTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String phone = (String)delegateTask.getVariable("phone");
        System.out.println("给这个电话号码:"+phone+"发送短信");
    }
}
