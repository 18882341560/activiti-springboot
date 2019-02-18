package com.gelin.activitispringboot.controller;

import com.gelin.activitispringboot.service.AuditProcessService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    public String leaveListHtml(){
        return "leaveList";
    }


}
