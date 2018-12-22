package com.gelin.activitispringboot;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create gl  2018/12/22
 * 连线
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class SequenceFlow {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                  .name("连线测试")
                  .addClasspathResource("processes/seuenceFlow.bpmn")
                  .deploy();
        System.out.println(deployment.getId());
    }


    @Test
    public void start(){
        runtimeService.startProcessInstanceByKey("seuenceFlow");
    }


    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery() // 创建人物查询对象
//                  .taskCandidateUser() // 组任务的办理人查询
//                    .executionId() // 执行对象id查询
                .taskAssignee("葛林1")  //指定个人任务查询
                .list();
        if(taskList != null && taskList.size()>0){
            for (Task task:taskList) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("执行定义ID:"+task.getProcessDefinitionId());
            }
        }
    }

    @Test
    public void completeMyTask(){
        // 需要任务id
        // 完成任务的同时，设置流程变量，使用流程变量用来指定完成后的下一次连线，对应 ${message=='xxx'}
        Map<String,Object> map = new HashMap<>();
        map.put("message","不重要");
        taskService.complete("35005",map);

        System.out.println("任务完成");
    }

    @Test
    public void completeMyTask1(){
        // 需要任务id
        // 完成任务的同时，设置流程变量，使用流程变量用来指定完成后的下一次连线，对应 ${message=='xxx'}
        Map<String,Object> map = new HashMap<>();
        map.put("message","重要");
        taskService.complete("42505",map);

        System.out.println("任务完成");
    }

}
