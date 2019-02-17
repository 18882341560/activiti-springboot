package com.gelin.activitispringboot.service.impl;

import com.gelin.activitispringboot.service.AuditProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
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
}
