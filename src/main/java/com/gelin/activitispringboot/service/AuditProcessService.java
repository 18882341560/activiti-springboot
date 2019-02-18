package com.gelin.activitispringboot.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:37
 * @describe
 */
public interface AuditProcessService {

    Object auditDeploy(String name, MultipartFile file) throws Exception;
    Object delDeployById(String id) throws Exception;
}
