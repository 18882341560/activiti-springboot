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
import java.util.List;

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
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi)
                .processDefinitionKey("firstPlan")
                .singleResult();
        String name = task.getName();
        if(name.equals("首检申请")){ //创建作业区领导审批任务
            List<User> user = baseDao.findAllUserByRoleName("作业区领导");
        }else if(name.equals("作业区领导审批")){//创建计量监督站审批任务

        }else if(name.equals("计量监督站审批")){//创建计量监督站安排任务

        }
    }
}
