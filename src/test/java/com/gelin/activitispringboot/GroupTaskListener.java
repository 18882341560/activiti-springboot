package com.gelin.activitispringboot;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * create gl  2019/1/8
 * 通过类来指定任务的办理人
 **/
public class GroupTaskListener implements TaskListener {
    private static final long serialVersionUID = 1637323558552997195L;

    //用来指定任务的办理人
    @Override
    public void notify(DelegateTask delegateTask) {
        //将用户 加入组任务
        delegateTask.addCandidateUser("");

    }
}
