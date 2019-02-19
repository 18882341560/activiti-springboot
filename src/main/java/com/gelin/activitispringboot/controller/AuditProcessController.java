package com.gelin.activitispringboot.controller;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.AuditRecords;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.AuditProcessService;
import com.gelin.activitispringboot.util.DateUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
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
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private TaskService taskService;
    @Autowired
    private BaseDao baseDao;

    // 部署请假流程
    @RequestMapping("/deploy")
    @ResponseBody
    public Object deployAudit(String name, MultipartFile file) throws Exception {
        return auditProcessService.auditDeploy(name,file);
    }

    @RequestMapping("/showPngHtml")
    public String showPngHtml(String id, Model model){
        model.addAttribute("deploymentId",id);
        return "processPng";
    }


    @RequestMapping("/showView")
    public void showView(HttpServletResponse response, String id) throws IOException {
        response.setContentType("image/jpeg/jpg/png/gif/bmp/tiff/svg");
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(id);
        String resourceName = "";
        if(resourceNames != null && resourceNames.size()>0){
            for (String name:resourceNames) {
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }
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


    @RequestMapping("/delDeployById")
    @ResponseBody
    public Object delDeployById(String id) throws Exception {
        return auditProcessService.delDeployById(id);
    }

    @RequestMapping("/leaveListHtml")
    public String leaveListHtml(Model model,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        //这里根据任务办理人来查询
//        List<AuditProcess> all = baseDao.findAllByCreateUserId(user.getId());
//        model.addAttribute("list",all);
        return "leaveList";
    }

    @RequestMapping("/leaveExamineListHtml")
    public String leaveExamineListHtml(Model model,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        //这里根据任务办理人来查询
        List<AuditProcess> all = auditProcessService.AuditProcessAssigneeList(user);
        model.addAttribute("list",all);
        return "leaveExamineList";
    }

    //新增请假申请
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

    @RequestMapping("/addLeave")
    public String addLeave(){
        return "addLeave";
    }


    //申请请假，开始流程
    @RequestMapping("/startAuditProcess")
    @ResponseBody
    public Object startAuditProcess(Integer id,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return auditProcessService.startAuditProcess(id,user.getId());
    }


    //跳转审核页面
    @RequestMapping("/examLeaveHtml")
    public String examLeaveHtml(Integer id,Model model){
        AuditProcess process = baseDao.findOneAuditProcessById(id);
        model.addAttribute("audit",process);
        return "examLeave";
    }

    //领导审核
    @RequestMapping("/exam")
    @ResponseBody
    public Object exam(String remark,Integer type,Integer auditId,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return auditProcessService.exam(remark,type,auditId,user);
    }


    //我的请假列表
    @RequestMapping("/myLeaveListHtml")
    public String myLeaveListHtml(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        List<AuditProcess> all = baseDao.findAllByCreateUserId(user.getId());
        model.addAttribute("list",all);
        return "myLeaveList";
    }

    @RequestMapping("/recordList")
    public String recordList(Integer id,Model model){
        AuditProcess ap = baseDao.findOneAuditProcessById(id);
        List<AuditRecords> record = baseDao.findAllRecordByAuditId(id);
        model.addAttribute("ap",ap);
        model.addAttribute("list",record);
        return "leaveRecord";
    }

}
