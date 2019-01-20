package com.gelin.activitispringboot;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * create gl  2019/1/7
 * 角色组
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class GroupUserTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService; // 历史相关的
    @Resource
    private IdentityService identityService;


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("角色组")
                .addClasspathResource("processes/GroupUser.bpmn")
                .deploy();
        System.out.println(deployment.getId());




        // act_id 开头的几张表，一般开发都不用这种
        identityService.saveGroup(identityService.newGroup("部门经理"));
        identityService.saveGroup(identityService.newGroup("总经理"));

        identityService.saveUser(identityService.newUser("张三"));
        identityService.saveUser(identityService.newUser("李四"));
        identityService.saveUser(identityService.newUser("王五"));
        identityService.createMembership("张三","部门经理");
        identityService.createMembership("李四","部门经理");
        identityService.createMembership("王五","总经理");

    }


    /**
     * 在启动的时候，判断流程是否结束，查询正在执行的执行对象，看有没有数据
     */
    @Test
    public void start(){
        //启动流程实例的同时，设置流程变量，指定任务的办理人
        Map<String,Object> map = new HashMap<>();
        map.put("userId","随便");
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("persionTask",map);
    }


}
