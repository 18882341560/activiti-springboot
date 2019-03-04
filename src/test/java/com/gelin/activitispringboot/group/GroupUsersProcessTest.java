package com.gelin.activitispringboot.group;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import com.google.common.collect.Maps;
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
import java.util.Map;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/4/004
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class GroupUsersProcessTest {


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
                .name("组任务测试流程")
                .addClasspathResource("processes/users/users.bpmn")
                .deploy();
        System.out.println("deployId:"+deploy.getId());
    }


    // 启动流程实例
    @Test
    public void startProcess(){

        //流程变量
        Map<String,Object> map = Maps.newHashMap();
        map.put("users","老胡,老李,老王");

        // 使用 key 来启动好处 默认寻找最新版本，版本多了的话
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("usersProcess",map);
        System.out.println("processInstanceId:"+processInstance.getId());
    }


    //查找我当前的待办任务
    @Test
    public void findMyTask(){
        List<Task> list = taskService.createTaskQuery() // 创建人物查询对象
                .processDefinitionKey("usersProcess")
                .taskAssignee("葛林")  //指定个人任务查询
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


    @Test
    public void findUsersTask(){
        List<Task> list = taskService.createTaskQuery() // 创建人物查询对象
                .processDefinitionKey("usersProcess")
                .taskCandidateUser("老王")
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
//        taskService.claim("155002","老胡");
        taskService.complete("155002");
    }



    //向组任务中添加成员
    @Test
    public void addGroupUser(){
        taskService.addCandidateUser("112505","小F");
    }


    //向组任务中删除成员
    @Test
    public void delGroupUser(){
        // 只会删除 type 为 候选者的一条数据，参与者这条数据没有删除，这样 这个候选者  与参与者 的区别就出来了
        taskService.deleteCandidateUser("112505","小B");
    }

}
