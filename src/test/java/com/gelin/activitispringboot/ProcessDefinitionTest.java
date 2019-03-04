package com.gelin.activitispringboot;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * create gl  2018/10/23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivitiSpringbootApplication.class)
public class ProcessDefinitionTest {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService; // 与正在执行的流程实例和执行对象相关的
    @Resource
    private TaskService taskService;

   /**
    *
    *跟流程定义相关的表  re开头的
    * act_re_deployment 部署对象表
    *act_re_procdef 流程定义表
    * act_ge_bytearray 资源文件表
    * act_ge_property 主键生成策略表  next.dbid 下一个主键是多少
    */

    // 流程部署 从classpath下
    @Test
    public void deploySimpleProcessDefinition() {
        Deployment deploy = repositoryService.createDeployment()
                .name("测试流程")
                .addClasspathResource("processes/test.bpmn")
                .deploy();
        System.out.println("部署Id:"+deploy.getId());
        System.out.println("部署名称:"+deploy.getName());
    }

    // 流程部署 zip下
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

    // 查询流程定义表
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
//                          .deploymentId() // 使用部署id查询
//                            .processDefinitionId() // 使用流程表的id查询
//                            .processDefinitionKey()  // 使用流程定义表的key查询   比较常用
//                              .processDefinitionNameLike() // 流程定义名称模糊查询
                //排序
                .orderByProcessDefinitionVersion().asc()  //升序
//                           .orderByProcessDefinitionVersion().desc()
//                            .orderByProcessDefinitionName().asc()
                //结果集
                .list();// 返回集合
//                                .singleResult() // 返回唯一结果集
//                                .count() //结果集数量
//                                  .listPage()  //分页

    }

    //删除流程定义
    @Test
    public void delProcess(){
        // 不带级联的删除，只能删除没有启动的流程，如果启动则会抛出异常
//         repositoryService.deleteDeployment("5001");

         // 级联删除，都可以删除 ，比较常用
         repositoryService.deleteDeployment("5001",true);
    }


    // 查看流程图
    @Test
    public void viewPic() throws IOException {

        String deplymentId = "17501";

        List<String> resourceNames = repositoryService.getDeploymentResourceNames(deplymentId);

        String resourceName = "";
        if(resourceNames != null && resourceNames.size()>0){
            for (String name:resourceNames) {
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }

        InputStream inputStream = repositoryService.getResourceAsStream(deplymentId, resourceName);
        File file = new File("D:/"+resourceName);
        FileUtils.copyInputStreamToFile(inputStream,file);
    }

}
