package com.gelin.activitispringboot.mail;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/6/006
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class MailTest {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;

    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deploy = repositoryService.createDeployment()
                .name("邮件测试流程")
                .addClasspathResource("processes/mail/mail.bpmn")
                .deploy();
        System.out.println(deploy);
    }

    // 启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("mailProcess");// 使用 key 来启动好处 默认寻找最新版本，版本多了的话

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId())
                .onlyChildExecutions()
                .singleResult();
        runtimeService.setVariable(execution.getId(),"phone","18882341560");

        runtimeService.trigger(execution.getId());
    }





}
