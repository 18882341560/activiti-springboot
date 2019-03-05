package com.gelin.activitispringboot.actviti.exclusive;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.util.SpringContextUtils;
import com.google.common.collect.Lists;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
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

        Integer money = (Integer) delegateTask.getVariable("money");
        System.out.println("money:"+money);


        List<String> list = null;
        if(money>1000){
            list = getUsers(baseDao.findAllUserByRoleName("总经理"));
        }else if(money >=500){
            list = Lists.newArrayList();
            list.add("小明");
        }else {
            list = getUsers(baseDao.findAllUserByRoleName("部门经理"));
        }
        delegateTask.addCandidateUsers(list);
    }

    private List<String> getUsers(List<User> user){
        return user.stream()
                .map(u -> u.getId().toString())
                .collect(Collectors.toList());
    }

}
