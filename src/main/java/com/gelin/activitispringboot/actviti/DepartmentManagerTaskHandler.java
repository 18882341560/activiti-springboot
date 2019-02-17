package com.gelin.activitispringboot.actviti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Auther 葛林
 * @Date 2019/2/17 17:47
 * @describe 请假流程  部门经理任务办理类
 */
public class DepartmentManagerTaskHandler implements TaskListener {
    private static final long serialVersionUID = -9195700150313933425L;

    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
