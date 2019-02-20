package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.FirstPlan;
import com.gelin.activitispringboot.service.FirstPlanService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Auther 葛林
 * @Date 2019/2/20/020 18
 * @Remarks
 */
@Service
@Transactional
public class FirstPlanServiceImpl implements FirstPlanService {

    @Autowired
    private BaseDao baseDao;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public Object save(FirstPlan firstPlan) throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FirstPlan");
        firstPlan.setProcessInstanceId(processInstance.getId());
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        //设置首检申请的待办人
        task.setAssignee(firstPlan.getCreateUserId().toString());
        baseDao.insertFirstPlan(firstPlan);
        return "申请成功";
    }
}
