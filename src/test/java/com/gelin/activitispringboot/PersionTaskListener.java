package com.gelin.activitispringboot;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * create gl  2019/1/8
 * 通过类来指定任务的办理人
 **/
public class PersionTaskListener implements TaskListener {
    private static final long serialVersionUID = 1637323558552997195L;

    //用来指定任务的办理人
    @Override
    public void notify(DelegateTask delegateTask) {

        //可以指定个人任务的办理人，也可以指定组任务的办理人
        //个人任务：通过类去数据库查询，将下一个任务的办理人查询获取，然后通过delegateTask.setAssignee()方法来指定任务的办理人
        delegateTask.setAssignee("办理人1");
        System.out.println("1112131");

    }
}
