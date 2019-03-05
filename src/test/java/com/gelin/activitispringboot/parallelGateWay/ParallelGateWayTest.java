package com.gelin.activitispringboot.parallelGateWay;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/5/005
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ParallelGateWayTest {



    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("并行网关测试")
                .addClasspathResource("processes/parallel/parallel.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }



    //启动，完成用户付款
    @Test
    public void start(){
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("parallelProcess");
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId(parallelGateWay.getId())
                .list();

        if(list != null && !list.isEmpty()){
            list.forEach(task->{
                String name = task.getName();
                if(Objects.equals(name,"用户付款")){
                   taskService.claim(task.getId(),"小明");
                   taskService.complete(task.getId());
                }
                if(Objects.equals(name,"商家收款")){
                   taskService.addCandidateUser(task.getId(),"小红");
                }
            });
        }
    }


    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery() // 创建人物查询对象
                .processDefinitionKey("parallelProcess")
                .taskCandidateUser("小红")
                .list();
        if(taskList != null && !taskList.isEmpty()){
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


    //完成当前任务
    @Test
    public void receivables(){
       taskService.complete("277502");
    }

}
