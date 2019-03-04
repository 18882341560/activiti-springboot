package com.gelin.activitispringboot;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
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
 * create gl  2019/1/6
 * 并行网关
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class parallelGateWayTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;



    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("并行网关")
                .addClasspathResource("processes/parallelGateWay.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }


    /**
     * 在启动的时候，判断流程是否结束，查询正在执行的执行对象，看有没有数据
     */
    @Test
    public void start(){
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("parallelGateWay");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(parallelGateWay.getId())
                .singleResult();
        if(processInstance == null){ //流程结束，查询历史数据
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(parallelGateWay.getId())
                    .singleResult();
        }
    }

    @Test
    public void completeMyTask(){
        // 需要任务id
        // 完成任务的同时，设置流程变量，使用流程变量用来指定完成后的下一次连线，对应 ${money...}
//        Map<String,Object> map = new HashMap<>();
//        map.put("money",400);
        taskService.complete("95002");

        System.out.println("任务完成");
    }


}
