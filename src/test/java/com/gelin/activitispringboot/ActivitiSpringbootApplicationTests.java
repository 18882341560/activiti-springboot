package com.gelin.activitispringboot;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ActivitiSpringbootApplicationTests {

   /**
    *
    * bpmn中文乱码还没有解决
    */

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Autowired
    private TaskService taskService;

    // 流程部署
    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deploy = repositoryService.createDeployment()
                .name("请假流程")
                .addClasspathResource("processes/helloworld.bpmn")
                .deploy();
        System.out.println(deploy);
    }

    // 启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance helloworld = runtimeService.startProcessInstanceByKey("helloworld");// 使用 key 来启动好处 默认寻找最新版本，版本多了的话
    }

    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery() // 创建人物查询对象
                                         .taskAssignee("张三")  //指定个人任务查询
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

    // 完成我的任务
    @Test
    public void completeMyTask(){
        // 需要任务id
        taskService.complete("7505");
        System.out.println("任务完成");
    }

}
