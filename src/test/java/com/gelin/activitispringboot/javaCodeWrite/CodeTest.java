package com.gelin.activitispringboot.javaCodeWrite;

import com.gelin.activitispringboot.ActivitiSpringbootApplication;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/12/012
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class CodeTest {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;

    // 流程部署
    @Test
    public void deploySimpleProcessDefinition() {

        byte[] bytes = new BpmnXMLConverter().convertToXML(createBpmnModel());

        //后面这个.bpmn20.xml不要变
        String processName = "javaCode3"+".bpmn20.xml";

        Deployment deployment = repositoryService.createDeployment()
                .name("javaCode3")
                .addString(processName, new String(bytes))
                .deploy();
    }

    // 启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance codeTest = runtimeService.startProcessInstanceByKey("codeTest");
    }

    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery() // 创建人物查询对象
                .taskCandidateUser("1")
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


    private BpmnModel createBpmnModel(){
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        process.setId("codeTest3");
        process.setName("codeTest3_process");
        bpmnModel.addProcess(process);

        process.addFlowElement(createStartEvent());
        process.addFlowElement(createUserTask("task1","经理审核"));
        process.addFlowElement(createUserTask("task2","葛林审核"));
        process.addFlowElement(createEndEvent());

        process.addFlowElement(createSequenceFlow("start","task1"));
        process.addFlowElement(createSequenceFlow("task1","task2"));
        process.addFlowElement(createSequenceFlow("task2","end"));

        new BpmnAutoLayout(bpmnModel).execute();

        return bpmnModel;
    }

    private UserTask createUserTask(String id,String name){
        UserTask userTask = new UserTask();
        List<String> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        userTask.setCandidateUsers(list);
        userTask.setName(name);
        userTask.setId(id);
        return userTask;
    }

    private SequenceFlow createSequenceFlow(String from,String to){
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setSourceRef(from);
        sequenceFlow.setTargetRef(to);
        return sequenceFlow;
    }

    private StartEvent createStartEvent(){
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    private EndEvent createEndEvent(){
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

}
