package com.gelin.activitispringboot;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * create gl  2019/1/7
 * 动态设置任务办理人
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class PersionTaskTest02 {


    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService; // 历史相关的


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("变量用户任务")
                .addClasspathResource("processes/PersionTask02.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }


    /**
     * 在启动的时候，判断流程是否结束，查询正在执行的执行对象，看有没有数据
     */
    @Test
    public void start(){
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("persionTask02");
    }


    /**
     * 指派任务给另一个人
     */
    @Test
    public void setAssigeneeTask(){
       taskService.setAssignee("107505","办理人2");
    }


}
