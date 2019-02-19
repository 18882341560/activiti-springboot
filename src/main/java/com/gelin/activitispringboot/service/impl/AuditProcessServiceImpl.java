package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.AuditProcessService;
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
             auditProcessList.add(baseDao.findOneAuditProcessByProcessInstanceId(pi.getId()));
          }
        });
        return auditProcessList;
    }

    @Override
    public Object startAuditProcess(Integer id,Integer userId) throws Exception {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("AuditProcess");

        //查询第一个任务，其实就是请假申请任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        //设置任务办理人
        taskService.setAssignee(task.getId(),userId.toString());

        //完成任务
        taskService.complete(task.getId());

        //将流程实例id记录数据表中
        AuditProcess auditProcess = AuditProcess.builder()
                .id(id)
                .processInstanceId(pi.getId())
                .status(2)
                .build();
        baseDao.updateAuditProcess(auditProcess);
        return "申请成功";
    }

    @Override
    public Object exam(String remark, Integer type,Integer auditId, User user) throws Exception {

        return null;
    }
}
