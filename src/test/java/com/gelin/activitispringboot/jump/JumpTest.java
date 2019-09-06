package com.gelin.activitispringboot.jump;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/8/5
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class JumpTest {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;
    @Resource
    private ManagementService managementService;

    @Test
    public void jump() {
        //获取流程定义
        Process process = repositoryService.getBpmnModel("ProjectDO:1:5004").getMainProcess();
        //获取目标节点定义
        FlowNode targetNode = (FlowNode) process.getFlowElement("_5");
        //删除当前运行任务
        String executionEntityId = managementService.executeCommand(new DeleteTaskCmd("32526",""));
        managementService.executeCommand(new DeleteTaskCmd("37505",""));
        //流程执行到来源节点
        managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode, executionEntityId));
    }
}
