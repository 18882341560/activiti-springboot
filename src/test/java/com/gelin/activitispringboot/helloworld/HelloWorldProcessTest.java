package com.gelin.activitispringboot.helloworld;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
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
 * @Date: 2019/3/4/004
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class HelloWorldProcessTest {



    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;

    // 流程部署
    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deploy = repositoryService.createDeployment()
                .name("helloWorld流程")
                .addClasspathResource("processes/helloworld/helloworld.bpmn")
                .deploy();
        System.out.println(deploy);
    }


    // 启动流程实例
    @Test
    public void startProcess(){
        // 使用 key 来启动好处 默认寻找最新版本，版本多了的话
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld");
        System.out.println("processInstanceId:"+processInstance.getId());
    }


    //查找我当前的待办任务
    @Test
    public void findMyTask(){
        List<Task> list = taskService.createTaskQuery() // 创建人物查询对象
                .processDefinitionKey("helloworld")
                .taskAssignee("胡成荣")  //指定个人任务查询
                .list();

        if(list != null && list.size()>0){
            list.forEach(task -> {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("执行定义ID:"+task.getProcessDefinitionId());
            });
        }
    }


    // 完成我的任务
    @Test
    public void completeMyTask(){
        taskService.complete("145005");
    }

}
