package com.gelin.activitispringboot.othertask;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/6/006
 * @description: 手动任务和接收任务
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ManualAndReceiveTest {


    /**
     * 手动任务定义了 BPM引擎之外的任务。用来对那些需要人来完成的工作进行建模，引擎不需要知道他是系统还是 UI接口。
     * 对引擎而言，手动任务是作为直接通过的活动处理的，流程执行到此会自动继续流程的执行
     */



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
        Deployment deploy = repositoryService.createDeployment()
                .name("手动任务与接收任务测试流程")
                .addClasspathResource("processes/othertask/manualAndReceive.bpmn")
                .deploy();
        System.out.println(deploy);
    }

    // 启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("manualAndReceiveProcess");
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("manualAndReceiveProcess")
                .list();
        System.out.println("执行手动任务之后，接收任务之前的任务数量:"+taskList.size());


        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .onlyChildExecutions()
                .singleResult();


        //开始等待

        runtimeService.setVariable(execution.getId(),"phone","19938494242");

        //执行接收任务,流程向下执行一步

        runtimeService.trigger(execution.getId());
    }
}
