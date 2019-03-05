package com.gelin.activitispringboot.exclusiveGateWay;

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
public class ExclusiveProcessTest {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;


    // 流程部署,todo 注意从上到下检索如果发现第一条决策结果为true或者没有设置条件的(默认为成立)，则流出。
    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deploy = repositoryService.createDeployment()
                .name("排他网关测试流程")
                .addClasspathResource("processes/exclusiveGateWay/exclusive.bpmn")
//                .addClasspathResource("processes/exclusiveGateWay/bugexclusive.bpmn")
                .deploy();
        System.out.println("deployId:"+deploy.getId());
    }


    // 启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusiveProcess");
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        //谁申请，就设置谁待办
        taskService.addCandidateUser(task.getId(),"葛林");
        System.out.println("processInstanceId:"+processInstance.getId());
    }


    @Test
    public void findUsersTask(){
        List<Task> list = taskService.createTaskQuery() // 创建人物查询对象
                .processDefinitionKey("exclusiveProcess")
                .taskCandidateUser("小红")
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


    // 完成申请
    @Test
    public void apply(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("money",510);
        taskService.claim("245005","葛林");
        taskService.complete("245005",map);
    }


    @Test
    public void exam(){
        taskService.claim("247505","小红");
        taskService.complete("247505");
    }



    //向组任务中添加成员
    @Test
    public void addGroupUser(){
        taskService.addCandidateUser("247505","小红");
    }


    //向组任务中删除成员
    @Test
    public void delGroupUser(){
        // 只会删除 type 为 候选者 candidate  的一条数据，参与者 participant 这条数据没有删除，这样 这个候选者  与参与者 的区别就出来了
        taskService.deleteCandidateUser("112505","小B");
    }

}
