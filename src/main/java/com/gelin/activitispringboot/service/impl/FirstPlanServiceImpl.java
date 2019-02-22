package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.FirstPlan;
import com.gelin.activitispringboot.model.FirstRecords;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.FirstPlanService;
import com.gelin.activitispringboot.util.DateUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        taskService.addCandidateUser(task.getId(),firstPlan.getCreateUserId().toString());
        baseDao.insertFirstPlan(firstPlan);
        return "申请成功";
    }

    @Override
    public List<FirstPlan> myAgencyTask(Integer userId) {
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("FirstPlan")
                .taskCandidateUser(userId.toString())
                .list();
        List<FirstPlan> list = new ArrayList<>();
        taskList.forEach(t->{
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                          .processInstanceId(t.getProcessInstanceId())
                          .singleResult();
            if(pi != null){
                 FirstPlan firstPlan = baseDao.findFirstPlanByProcessInstanceId(pi.getId());
                 if(firstPlan != null) list.add(firstPlan);
            }
        });
        return list;
    }

    @Override
    public Object applyFirstPlan(Integer id) throws Exception {
        FirstPlan firstPlan = baseDao.findOneFirstPlanById(id);
        if(firstPlan == null){
            return new RuntimeException("首检计划没有找到!");
        }

        //查找当前需要办理的任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(firstPlan.getProcessInstanceId())
                .singleResult();
        //拾取任务，指定该任务是谁办理的
        taskService.claim(task.getId(),id.toString());
        taskService.complete(task.getId());
        //改变首检的状态为审核
        FirstPlan fp = FirstPlan.builder()
                .id(id)
                .status(2)
                .build();
        baseDao.updateFirstPlan(fp);
        return "申请成功";
    }

    @Override
    public Object exam(String remarks, Integer type,Integer id,User user) throws Exception {

        FirstRecords firstRecords = FirstRecords.builder()
                .firstPlanId(id)
                .examStatus(type)
                .operationDate(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss())
                .operationUserId(user.getId())
                .remarks(remarks)
                .build();

        FirstPlan fp = FirstPlan.builder()
                .id(id)
                .build();

        FirstPlan firstPlan = baseDao.findOneFirstPlanById(id);

        //当前的任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(firstPlan.getProcessInstanceId())
                .singleResult();

        String taskName = task.getName();

        //这里设置流程变量
        Map<String,Object> map = new HashMap<>();

        if(type == 1){ //批准
            if(taskName.equals("计量监督站审批")){ //说明审核已经完成了，需要安排首检时间
               fp.setStatus(3);
            }else {
                fp.setStatus(2);
            }
            map.put("exam","批准");
        }else { //驳回，流程结束，申请需要从新填写数据
            map.put("exam","驳回");
            fp.setStatus(4);
        }
        taskService.claim(task.getId(),user.getId().toString());
        taskService.complete(task.getId(),map);
        baseDao.insertFirstRecords(firstRecords);
        baseDao.updateFirstPlan(fp);
        return "审核成功";
    }

    @Override
    public Object arrange(FirstPlan firstPlan,Integer userId) throws Exception {
        FirstPlan fp = baseDao.findOneFirstPlanById(firstPlan.getId());
        //当前的任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(fp.getProcessInstanceId())
                .singleResult();
        taskService.claim(task.getId(),userId.toString());
        taskService.complete(task.getId());
        baseDao.updateFirstPlan(firstPlan);
        return "安排成功";
    }
}
