package com.gelin.activitispringboot;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create gl  2019/1/7
 * 组任务办理人设置
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class GroupTaskTest {


    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService; // 历史相关的


    /**
     * act_ru_identitylink（任务办理人表  个人/组）    字段   TYPE_    participant  参与者，记录的是流程实例id   candidate  候选者，记录的是任务id
     */


    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .name("组任务")
                .addClasspathResource("processes/group/GroupTask1.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }


    /**
     *  使用流程变量设置组成员,可以在启动的时候    11,22,33 这样的
     */
    @Test
    public void start(){
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("groupTask1");
    }


    // 正在执行的办理人表，历史表与这个一样
    @Test
    public void findRunPersionTask(){
        List<IdentityLink> linksForTask = taskService.getIdentityLinksForTask("112505");

    }



    //拾取任务，将组任务分给个人任务，指定任务的办理人这个字段不为null，一般在组任务 中该任务执行了后，将操作人拾取到办理人中
    @Test
    public void cliam(){
        // 将组任务分配给个人任务，taskId,userId
        // 分配的个人任务   可以是组任务中的成员，也可以是非组任务中的成员
        // 拾取过后，原先的小A，小B。。。，就不能在查询了，只能大F能查询的到，相当于将组任务 变成了个人任务
        taskService.claim("112505","大F");
    }


    //将个人任务 回退到组任务,前提：之前一定是个组任务
    @Test
    public void setAssagee(){
        taskService.setAssignee("112505",null);
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
