package com.gelin.activitispringboot;

import com.gelin.activitispringboot.actviti.User;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * create gl  2018/10/25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ProcessInstanceTest {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService; // 历史相关的


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
        taskService.complete("12505");
        System.out.println("任务完成");
    }


    @Test
    public void findProcessStatus(){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("2501") // 2501 是流程实例id，只有一个，2502 是执行对象id，可以有多个，比如2503
                .singleResult();
        if(processInstance == null){
            System.out.println("流程已经结束");
        }else {
            System.out.println("流程正在执行");
        }
    }


    // 查询历史任务
    @Test
    public void findHistory(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("张三")
                .list();
        for (HistoricTaskInstance ht:list) {
            System.out.println("id"+ht.getId());
            System.out.println("name"+ht.getName());
        }
    }

    // 查询历史流程实例
    @Test
    public void findHistoryProcessInstance(){
        HistoricProcessInstance singleResult = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId("2501")
                .singleResult();
    }


    // 设置流程变量
    @Test
    public void setVaribles(){
         /*与任务*/
//        taskService.setVariableLocal("12505","请假天数",3);
//        taskService.setVariable("12505","请假日期",new Date());
//        taskService.setVariable("12505","请假原因","相亲");

//        taskService.setVariableLocal(); // 与不加local的区别，加了过后，与任务id绑定

        /**
         * act_ru_variable 正在执行的流程变量表
         * act_hi_varinst 历史表
         */


        // 使用对象的方式
        User user = new User();
        user.setId(1);
        user.setName("书都上");
        taskService.setVariable("17502","人员信息",user); // 使用对象，对象必须使用序列化
        /**
         * 当对象放置到流程变量中，对象属性不能在发生变化，否则报错 Couldn't deserialize object in variable,
         * 解决：固定序列化版本，生成id
         */
    }

    // 获取流程变量
    @Test
    public void getVaribles(){
//       Integer day = (Integer) taskService.getVariable("17502","请假天数");
//       Date d = (Date) taskService.getVariable("17502","请假日期");
//       String s = (String) taskService.getVariable("17502","请假原因");
//        System.out.println(day); //为null，上面与任务绑定了
//        System.out.println(d);
//        System.out.println(s);
       User user = (User) taskService.getVariable("17502","人员信息");
        System.out.println(user);
    }


    // 模拟场景
    @Test
    public void simulation(){
//        runtimeService.setVariable();//表示使用执行对象id，和流程变量的名称，设置流程变量的值，一次只能设置一个值
//        runtimeService.setVariables(); // 表示使用执行对象id和map集合设置流程变量，map的key就是流程变量的名称，value就是设置的值，一次设置多个值
//          taskService 与上面的相同
//        runtimeService.startProcessInstanceByKey(String s1,Map<String,Object>) //启动流程实例的时候，同事设置流程变量的值
//        taskService.complete(String s1,Map<String,Object>); //完成任务的时候设置流程变量的值

        // 获取流程变量
//        runtimeService.getVariable() // 执行对象id和流程变量的名称 获取值
//        runtimeService.getVariables() //执行对象id获取所有的流程变量
//        runtimeService.getVariables(String s1,Connection c) // 执行对象id和指定集合（流程变量名称）来获取值
        // 任务是一样的
    }


}
