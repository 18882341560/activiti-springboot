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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/17 17:47
 * @describe 请假流程  部门经理任务办理类,需要创建到这个任务节点的时候执行
 */
@Component
public class DepartmentManagerTaskHandler implements TaskListener {
    private static final long serialVersionUID = -9195700150313933425L;

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
                .processDefinitionKey("AuditProcess")
                .singleResult();
        System.out.println("taskName1:"+task.getName());

        //指定下一个任务的办理人
        List<User> user = baseDao.findAllUserByRoleName("部门经理");
        delegateTask.setAssignee(user.get(0).getId().toString());
    }
}
