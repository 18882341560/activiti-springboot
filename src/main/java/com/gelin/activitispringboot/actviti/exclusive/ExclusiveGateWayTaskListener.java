package com.gelin.activitispringboot.actviti.exclusive;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class ExclusiveGateWayTaskListener implements TaskListener {


    @Autowired
    private BaseDao baseDao;
    @Resource
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {

        if(baseDao == null){
            baseDao = (BaseDao) SpringContextUtils.getBeanByClass(BaseDao.class);
        }
        if(taskService == null){
            taskService = (TaskService)SpringContextUtils.getBeanByClass(TaskService.class);
        }


        System.out.println("name:"+delegateTask.getName());

    }


}
