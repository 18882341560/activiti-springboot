package com.gelin.activitispringboot.actviti.parallel;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/5/005
 * @description:
 */
@Component
public class ParallelTaskListener implements TaskListener {

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

        String name = delegateTask.getName();
        System.out.println(delegateTask.getName());

        if(Objects.equals(name,"用户收货")){
           delegateTask.addCandidateUser("小明");
        }

        if(Objects.equals(name,"商家发货")){
            delegateTask.addCandidateUser("小红");
        }

        if(Objects.equals(name,"商家确认收货")){
            delegateTask.addCandidateUser("小红");
        }

    }


}
