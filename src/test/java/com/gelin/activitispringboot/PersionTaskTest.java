package com.gelin.activitispringboot;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
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
 * 动态设置任务办理人
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class PersionTaskTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService; // 历史相关的


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("变量用户任务")
                .addClasspathResource("processes/PersionTask.bpmn")
                .deploy();
        System.out.println(deployment.getId());
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
