package com.gelin.activitispringboot;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * create gl  2019/1/7
 * 接收活动，等待活动
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ReceiveTaskTest {


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
        Deployment deployment = repositoryService.createDeployment()
                .name("等待活动")
                .addClasspathResource("processes/receive.bpmn")
                .deploy();
        System.out.println(deployment.getId());
    }


    /**
     * 在启动的时候，判断流程是否结束，查询正在执行的执行对象，看有没有数据
     */
    @Test
    public void start(){
        ProcessInstance parallelGateWay = runtimeService.startProcessInstanceByKey("receiveTask");
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//                .processInstanceId(parallelGateWay.getId())
//                .singleResult();

        Execution receiveTask = runtimeService.createExecutionQuery()
                .processInstanceId(parallelGateWay.getId())//使用流程实例id来查询
                .activityId("receiveTask") //使用活动id来查询，一般是与流程实例两个搭配来查询
                .singleResult();

        /**
         * 使用流程变量设置当前销售额，用来传递业务参数
         */

        runtimeService.setVariable(receiveTask.getId(),"汇总当日销售额",21000);

        /**
         * 向后执行一步，如果流程处于等待状态，使得流程继续执行
         */

        runtimeService.signalEventReceived(receiveTask.getId());

        /**
         * 从流程变量中获取当日销售总额
         */

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(parallelGateWay.getId())//使用流程实例id来查询
                .activityId("receiveTask")
                .singleResult();

        Integer variable = (Integer) runtimeService.getVariable(execution.getId(), "汇总当日销售额");

        System.out.println("给老板发送短信，当日销售额："+variable);

        /**
         * 向后执行一步，如果流程处于等待状态，使得流程继续执行
         */

        runtimeService.signalEventReceived(execution.getId());

    }


}
