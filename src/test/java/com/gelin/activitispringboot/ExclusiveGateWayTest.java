package com.gelin.activitispringboot;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * create gl  2018/12/22
 * 排他网关,idea中的Activiti插件是不用制定默认流程的，只要我们不设置条件，那就是默认的连接线
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ExclusiveGateWayTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;



    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("排他网关")
                .addClasspathResource("processes/exclusiveGateWay.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }


    @Test
    public void start(){
        runtimeService.startProcessInstanceByKey("gateWay");
    }


    @Test
    public void completeMyTask(){
        // 需要任务id
        // 完成任务的同时，设置流程变量，使用流程变量用来指定完成后的下一次连线，对应 ${money...}
        Map<String,Object> map = new HashMap<>();
        map.put("money",400);
        taskService.complete("80005",map);

        System.out.println("任务完成");
    }

}
