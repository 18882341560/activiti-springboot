package com.gelin.activitispringboot.controller;

import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.AuditProcessService;
import com.gelin.activitispringboot.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:18
 * @describe
 */
@Controller
@RequestMapping("/audit")
public class AuditProcessController {

    @Autowired
    private AuditProcessService auditProcessService;

    /**
     * 部署流程
     * @param name 流程名称
     * @param file 流程压缩包
     * @return
     * @throws Exception
     */
    @RequestMapping("/deploy")
    @ResponseBody
    public Object deployAudit(String name, MultipartFile file) throws Exception {
        return auditProcessService.auditDeploy(name,file);
    }

    /**
     * 查看总流程图的页面
     * @param id 部署的id
     * @param model
     * @return
     */
    @RequestMapping("/showPngHtml")
    public String showPngHtml(String id, Model model){
        model.addAttribute("deploymentId",id);
        return "processPng";
    }


    /**
     * 生成流程图
     * @param response
     * @param id  部署的id
     * @throws Exception
     */
    @RequestMapping("/showView")
    public void showView(HttpServletResponse response, String id) throws Exception {
       auditProcessService.showView(response,id);
    }


    /**
     * 删除部署流程
     * @param id  部署的id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delDeployById")
    @ResponseBody
    public Object delDeployById(String id) throws Exception {
        return auditProcessService.delDeployById(id);
    }


    /**
     * 请假任务办理列表
     * @param model
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/leaveExamineListHtml")
    public String leaveExamineListHtml(Model model,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        //这里根据任务办理人来查询
        List<AuditProcess> all = auditProcessService.AuditProcessAssigneeList(user);
        model.addAttribute("list",all);
        return "leaveExamineList";
    }

    /**
     * 新增请假申请
     * @param auditProcess
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object save(AuditProcess auditProcess, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        auditProcess.setStatus(1);
        auditProcess.setCreateUserId(user.getId());
        auditProcess.setName(user.getName());
        auditProcess.setCreateTime(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss());
        return auditProcessService.save(auditProcess);
    }

    /**
     * 新增请假页面
     * @return
     */
    @RequestMapping("/addLeave")
    public String addLeave(){
        return "addLeave";
    }


    /**
     * 申请请假，开始流程
     * @param id 请假id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/startAuditProcess")
    @ResponseBody
    public Object startAuditProcess(Integer id,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return auditProcessService.startAuditProcess(id,user.getId());
    }


    /**
     * 跳转审核页面
     * @param id 请假id
     * @param model
     * @return
     */
    @RequestMapping("/examLeaveHtml")
    public String examLeaveHtml(Integer id,Model model) throws Exception {
        auditProcessService.examLeaveHtml(id,model);
        return "examLeave";
    }

    /**
     * 领导审核
     * @param remark 意见
     * @param type   审核类型  1 通过 2 驳回
     * @param auditId 请假id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/exam")
    @ResponseBody
    public Object exam(String remark,Integer type,Integer auditId,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return auditProcessService.exam(remark,type,auditId,user);
    }


    /**
     * 我的请假列表
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/myLeaveListHtml")
    public String myLeaveListHtml(HttpSession session,Model model) throws Exception {
        User user = (User) session.getAttribute("user");
        model.addAttribute("list",auditProcessService.findMyAuditList(user));
        return "myLeaveList";
    }

    /**
     * 审核记录列表页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/recordList")
    public String recordList(Integer id,Model model) throws Exception {
        auditProcessService.recordList(id,model);
        return "leaveRecord";
    }

    /**
     * 查看流程执行任务图
     * @param processInstanceId
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getProcessImage")
    public void getProcessImage(String processInstanceId,HttpServletResponse response) throws Exception {
       auditProcessService.getProcessImage2(processInstanceId,response);
    }

}
