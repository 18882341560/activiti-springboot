package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.AuditRecords;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.AuditProcessService;
import com.gelin.activitispringboot.util.DateUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:37
 * @describe
 */
@Service
@Transactional
public class AuditProcessServiceImpl implements AuditProcessService {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Autowired
    private BaseDao baseDao;

    @Override
    public Object login(User user, HttpSession session) throws Exception {
        User userLogin = baseDao.findOneByLogin(user);
        if(userLogin != null){
            session.setAttribute("user",userLogin);
            return "登陆成功";
        }
        return "账号密码错误";
    }

    @Override
    public Object auditDeploy(String name, MultipartFile file) throws Exception{
        InputStream is = file.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(is);
        repositoryService.createDeployment()
                         .name(name)
                         .addZipInputStream(zipInputStream)
                         .deploy();
        return "部署成功";
    }

    @Override
    public Object delDeployById(String id) throws Exception {
        repositoryService.deleteDeployment(id,true);
        return "删除成功";
    }

    @Override
    public Object save(AuditProcess auditProcess) throws Exception {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("AuditProcess");

        //查询第一个任务，其实就是请假申请任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        //设置任务办理人
        taskService.setAssignee(task.getId(),auditProcess.getCreateUserId().toString());
        auditProcess.setProcessInstanceId(pi.getId());
        baseDao.insertAuditProcess(auditProcess);
        return "新增成功";
    }

    @Override
    public List<AuditProcess> AuditProcessAssigneeList(User user) throws Exception {
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("AuditProcess")
                .taskAssignee(user.getId().toString())
                .list();
        List<AuditProcess> auditProcessList = new ArrayList<>();
        taskList.forEach( a->{
          ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(a.getProcessInstanceId())
                        .singleResult();
          if(pi != null){
             AuditProcess ap = baseDao.findOneAuditProcessByProcessInstanceId(pi.getId());
             if(ap != null){
                 auditProcessList.add(ap);
             }
          }
        });
        return auditProcessList;
    }

    @Override
    public Object startAuditProcess(Integer id,Integer userId) throws Exception {

//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("AuditProcess");
        AuditProcess ap = baseDao.findOneAuditProcessById(id);
        //查询第一个任务，其实就是请假申请任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(ap.getProcessInstanceId())
                .singleResult();
//
//        //设置任务办理人
//        taskService.setAssignee(task.getId(),userId.toString());

        //完成任务
        taskService.complete(task.getId());

        //将流程实例id记录数据表中
        AuditProcess auditProcess = AuditProcess.builder()
                .id(id)
                .status(2)
                .build();
        baseDao.updateAuditProcess(auditProcess);
        return "办理成功";
    }

    @Override
    public Object exam(String remark, Integer type,Integer auditId, User user) throws Exception {
        AuditProcess auditProcess = baseDao.findOneAuditProcessById(auditId);
        AuditProcess ap = AuditProcess.builder()
                .id(auditId)
                .build();
        AuditRecords auditRecords = AuditRecords.builder()
                .auditProcessId(auditId)
                .examineUserId(user.getId())
                .remarks(remark)
                .examineTime(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss())
                .build();
        Task task = taskService.createTaskQuery()
                .processInstanceId(auditProcess.getProcessInstanceId())
                .singleResult();

        //这里设置流程变量
        Map<String,Object> map = new HashMap<>();

        if(task.getName().equals("部门经理审批")){
            if(type == 1){//通过
                ap.setStatus(2);
                auditRecords.setExamineStatus(1);
                map.put("auditType","批准");
            }else { //驳回
                ap.setStatus(4);
                auditRecords.setExamineStatus(2);
                map.put("auditType","驳回");
            }
            taskService.complete(task.getId(),map);
        }else if(task.getName().equals("总经理审批")){
            if(type == 1){//通过
                ap.setStatus(3);
                auditRecords.setExamineStatus(1);
                map.put("auditType","批准");
                taskService.complete(task.getId(),map);
            }else { //驳回
                ap.setStatus(4);
                auditRecords.setExamineStatus(2);
                map.put("auditType","驳回");
                taskService.complete(task.getId(),map);
                //返回开始申请流程，设置任务的办理人,还要设置请假单的状态
                task = taskService.createTaskQuery()
                        .processInstanceId(auditProcess.getProcessInstanceId())
                        .singleResult();
                taskService.setAssignee(task.getId(),auditProcess.getCreateUserId().toString());
                ap.setStatus(1);
            }
        }

        baseDao.updateAuditProcess(ap);
        baseDao.insertAuditRecords(auditRecords);

        return "审核成功";
    }
}
