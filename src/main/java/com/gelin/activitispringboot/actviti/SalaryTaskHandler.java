package com.gelin.activitispringboot.actviti;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther 葛林
 * @Date 2019/2/20/020 17
 * @Remarks
 */
@Component
public class SalaryTaskHandler implements TaskListener {

    private static final long serialVersionUID = 8180211132909051505L;
    @Autowired
    private BaseDao baseDao;
    @Resource
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        if(baseDao == null){
            baseDao = (BaseDao)SpringContextUtils.getBeanByClass(BaseDao.class);
        }
        if(taskService == null){
            taskService = (TaskService)SpringContextUtils.getBeanByClass(TaskService.class);
        }

        String pi = delegateTask.getProcessInstanceId();
        //在执行监听之前的任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi)
                .processDefinitionKey("FirstPlan")
                .singleResult();
        String name = task.getName();
        List<User> user = new ArrayList<>();
        if(name.equals("首检申请")){ //创建作业区领导审批任务
            user = baseDao.findAllUserByRoleName("作业区领导");
        }else if(name.equals("作业区领导审批")){//创建计量监督站审批任务
            user = baseDao.findAllUserByRoleName("计量监督站领导");
        }else if(name.equals("计量监督站审批")){//创建计量监督站安排任务
            user = baseDao.findAllUserByRoleName("计量监督站领导");
        }
        delegateTask.addCandidateUsers(getUsers(user));
    }

    private List<String> getUsers(List<User> user){
        return user.stream()
                .map(u -> u.getId().toString())
                .collect(Collectors.toList());
    }
}
