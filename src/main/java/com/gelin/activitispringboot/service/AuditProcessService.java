package com.gelin.activitispringboot.service;

import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:37
 * @describe
 */
public interface AuditProcessService {
    Object login(User user, HttpSession session) throws Exception;
    Object auditDeploy(String name, MultipartFile file) throws Exception;
    Object delDeployById(String id) throws Exception;
    Object save(AuditProcess auditProcess) throws Exception;
    Object startAuditProcess(Integer id,Integer userId) throws Exception;
    List<AuditProcess> AuditProcessAssigneeList(User user) throws Exception;
    Object exam(String remark,Integer type,Integer auditId,User user) throws Exception;
}
