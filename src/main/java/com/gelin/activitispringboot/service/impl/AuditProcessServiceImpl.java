package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.AuditRecords;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.AuditProcessService;
import com.gelin.activitispringboot.util.BpmsActivityTypeEnum;
import com.gelin.activitispringboot.util.DateUtils;
import com.gelin.activitispringboot.util.DefaultProcessDiagramGenerator;
import com.gelin.activitispringboot.util.UtilMisc;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:37
 * @describe
 */
@Service
@Transactional
public class AuditProcessServiceImpl implements AuditProcessService {


    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Autowired
    private BaseDao baseDao;

    @Override
    public void showView(HttpServletResponse response, String id) throws Exception {
        response.setContentType("image/jpeg/jpg/png/gif/bmp/tiff/svg");
        //两个资源文件 名称  一个是png结尾的图片名称，一个是bpmn结尾的流程资源
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(id);
        String resourceName = "";
        if(resourceNames != null && resourceNames.size()>0){
            for (String name:resourceNames) {
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }
        //activiti的api，根据部署id，资源名称，查找资源
        InputStream is = repositoryService.getResourceAsStream(id, resourceName);
        OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        while(is.read(b)!= -1) {
            os.write(b);
        }
        is.close();
        os.flush();
        os.close();
    }

    @Override
    public Object login(User user, HttpSession session) throws Exception {
        User userLogin = baseDao.findOneByLogin(user);
        if(userLogin != null){
            session.setAttribute("user",userLogin);
            return "登陆成功";
        }
        return "账号密码错误";
    }

    @Override
    public Object auditDeploy(String name, MultipartFile file) throws Exception{
        InputStream is = file.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(is);
        repositoryService.createDeployment()
                         .name(name)
                         .addZipInputStream(zipInputStream)
                         .deploy();
        return "部署成功";
    }

    @Override
    public Object delDeployById(String id) throws Exception {
        //true 级联删除
        repositoryService.deleteDeployment(id,true);
        return "删除成功";
    }

    @Override
    public Object save(AuditProcess auditProcess) throws Exception {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("AuditProcess");

        //查询第一个任务，其实就是请假申请任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        //设置任务办理人,为当前提交人
        taskService.setAssignee(task.getId(),auditProcess.getCreateUserId().toString());
        auditProcess.setProcessInstanceId(pi.getId());
        baseDao.insertAuditProcess(auditProcess);
        return "新增成功";
    }

    @Override
    public List<AuditProcess> AuditProcessAssigneeList(User user) throws Exception {
        //查询我当前有哪些待办理的任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("AuditProcess")
                .taskAssignee(user.getId().toString())
                .list();
        List<AuditProcess> auditProcessList = new ArrayList<>();
        taskList.forEach( t->{
             AuditProcess ap = baseDao.findOneAuditProcessByProcessInstanceId(t.getProcessInstanceId());
             if(ap != null) auditProcessList.add(ap);
        });
        return auditProcessList;
    }

    @Override
    public Object startAuditProcess(Integer id,Integer userId) throws Exception {

        AuditProcess ap = baseDao.findOneAuditProcessById(id);

        //查询第一个任务，其实就是请假申请任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(ap.getProcessInstanceId())
                .singleResult();

        //完成任务
        taskService.complete(task.getId());

        //将流程实例id记录数据表中
        AuditProcess auditProcess = AuditProcess.builder()
                .id(id)
                .status(2)
                .build();
        baseDao.updateAuditProcess(auditProcess);
        return "办理成功";
    }

    @Override
    public Object exam(String remark, Integer type,Integer auditId, User user) throws Exception {
        //查找请假
        AuditProcess auditProcess = baseDao.findOneAuditProcessById(auditId);
        //设置更改审核请假状态
        AuditProcess ap = AuditProcess.builder()
                .id(auditId)
                .build();
        //审核记录
        AuditRecords auditRecords = AuditRecords.builder()
                .auditProcessId(auditId)
                .examineUserId(user.getId())
                .remarks(remark)
                .examineTime(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss())
                .build();

        //当前任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(auditProcess.getProcessInstanceId())
                .singleResult();

        //这里设置流程变量
        Map<String,Object> map = new HashMap<>();

        if(type == 1){
            if(task.getName().equals("部门经理审批")){
                ap.setStatus(2);
            }else if(task.getName().equals("总经理审批")){
                ap.setStatus(3);
            }
            auditRecords.setExamineStatus(1);
            map.put("auditType","批准");
            taskService.complete(task.getId(),map);
        }else {
            ap.setStatus(4);
            auditRecords.setExamineStatus(2);
            map.put("auditType","驳回");
            taskService.complete(task.getId(),map);
            //返回开始申请流程，设置任务的办理人,还要设置请假单的状态
            task = taskService.createTaskQuery()
                    .processInstanceId(auditProcess.getProcessInstanceId())
                    .singleResult();
            taskService.setAssignee(task.getId(),auditProcess.getCreateUserId().toString());
            ap.setStatus(1);
        }
        baseDao.updateAuditProcess(ap);
        baseDao.insertAuditRecords(auditRecords);
        return "审核成功";
    }

    @Override
    public List<AuditProcess> findMyAuditList(User user) throws Exception {
        return baseDao.findAllByCreateUserId(user.getId());
    }

    @Override
    public void getProcessImage(String processInstanceId, HttpServletResponse response) throws Exception {
        //设置不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if(historicProcessInstance == null){
             throw new RuntimeException("历史流程实例为空");
        }

        // 获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> hisList =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .orderByHistoricActivityInstanceId()
                        .asc()
                        .list();

        // 构造已执行的节点ID集合,可以判断endtime 不为空的是已经执行了的
        List<String> executedActivityIdList = new ArrayList<String>();
        for (HistoricActivityInstance activityInstance : hisList) {
            if(activityInstance.getEndTime() != null){
                executedActivityIdList.add(activityInstance.getActivityId());
            }
        }

        // 获取bpmnModel
        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        // 获取流程已发生流转的线ID集合
        List<String> flowIds = this.getExecutedFlows(bpmnModel, hisList);

        // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream imageStream = processDiagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                executedActivityIdList,
                flowIds,
                "宋体",
                "微软雅黑",
                "黑体",
                null,
                2.0);

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        imageStream.close();
    }


    @Override
    public void examLeaveHtml(Integer id, Model model) throws Exception {
        AuditProcess process = baseDao.findOneAuditProcessById(id);
        model.addAttribute("audit",process);
    }

    @Override
    public void recordList(Integer id,Model model) throws Exception {
        AuditProcess ap = baseDao.findOneAuditProcessById(id);
        List<AuditRecords> record = baseDao.findAllRecordByAuditId(id);
        model.addAttribute("ap",ap);
        model.addAttribute("list",record);
    }

    //获取所有执行的连线的id
    private List<String> getExecutedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 流转线ID集合
        List<String> flowIdList = new ArrayList<String>();
        // 全部活动实例
        List<FlowNode> historicFlowNodeList = new LinkedList<FlowNode>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstanceList = new LinkedList<HistoricActivityInstance>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            historicFlowNodeList.add((FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true));
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicActivityInstance);
            }
        }

        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        FlowNode currentFlowNode = null;
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstanceList) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的
             * 满足如下条件认为已已流转：
             * 1.当前节点是并行网关或包含网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最近的流转节点视为有效流转
             */
            FlowNode targetFlowNode = null;
            if (BpmsActivityTypeEnum.PARALLEL_GATEWAY.getType().equals(currentActivityInstance.getActivityType())
                    || BpmsActivityTypeEnum.INCLUSIVE_GATEWAY.getType().equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicFlowNodeList.contains(targetFlowNode)) {
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                List<Map<String, String>> tempMapList = new LinkedList<Map<String,String>>();
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    //sequenceFlow 这个是连线对象
                    int a = 0;
                    for (HistoricActivityInstance historicActivityInstance : finishedActivityInstanceList) {
                        //historicActivityInstance 这个是任务节点对象

                        //上一个节点的id

                        String beforeId = "";
                        if(a>0){
                            beforeId = finishedActivityInstanceList.get(a-1).getActivityId();
                        }else {
                            beforeId = finishedActivityInstanceList.get(0).getActivityId();
                        }

                        /**
                         * 判断这个连线的结束节点 与任务节点是否相同，拿到这个连线的id，如果没有匹配到，说明这个节点是开始节点
                         * 如果匹配到了，说明这条连线是连接这两个节点的连线
                         */
                        if (sequenceFlow.getSourceRef().equals(beforeId) && sequenceFlow.getTargetRef().equals(historicActivityInstance.getActivityId())) {
                            //下面是拿到连线的id，封装成一个map对象
                            tempMapList.add(
                                    UtilMisc.toMap("flowId", sequenceFlow.getId(),
                                            "activityStartTime",
                                            String.valueOf(historicActivityInstance.getStartTime().getTime())));
                        }
                        a = a +1;
                    }
                }

                // 遍历匹配的集合，取得开始时间最早的一个
                long earliestStamp = 0L;
                String flowId = null;
                for (Map<String, String> map : tempMapList) {
                    long activityStartTime = Long.valueOf(map.get("activityStartTime"));
                    if (earliestStamp == 0 || earliestStamp >= activityStartTime) {
                        earliestStamp = activityStartTime;
                        flowId = map.get("flowId");
                    }
                }
                flowIdList.add(flowId);
            }
        }
        return flowIdList;
    }

}
