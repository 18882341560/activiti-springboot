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

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * create gl  2018/10/25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ProcessInstanceTest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Autowired
    private TaskService taskService;


    /**
     * 流程实例，执行对象，任务   ru开头的
     *act_ru_execution 正在执行的执行对象表   没有分支，执行对象id 和 流程实例 proc_inst_id是相同的
     * 一个流程 流程实例只有一个，执行对象可以存在多个
     *
     *act_hi_procinst 流程实例的历史表 end_time 为null 表示没有结束，正在执行
     *
     * act_ru_task  正在执行的任务表  只有节点是userTask 的才有数据
     *
     * act_hi_taskinst 任务历史表  只有节点是userTask 的才有数据
     *
     * act_hi_actinst  所有活动节点的历史表
     *
     */

    @Test
    public void deploySimpleProcessDefinition_Zip() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deploy = repositoryService.createDeployment()
                .name("请假流程")
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("部署Id:"+deploy.getId());
        System.out.println("部署名称:"+deploy.getName());
    }

    @Test
    public void startProcess(){
        ProcessInstance helloworld = runtimeService.startProcessInstanceByKey("helloworld");// 使用 key 来启动好处 默认寻找最新版本，版本多了的话
        System.out.println(helloworld.getId());
    }


    @Test
    public void findMyTask(){
        List<Task> taskList = taskService.createTaskQuery() // 创建人物查询对象
//                  .taskCandidateUser() // 组任务的办理人查询
//                    .executionId() // 执行对象id查询
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

    // 完成我的任务 act_ru_task
    @Test
    public void completeMyTask(){
        // 需要任务id
        taskService.complete("2505");
        System.out.println("任务完成");
    }


}
