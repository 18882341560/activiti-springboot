package com.gelin.activitispringboot.actviti;

import com.gelin.activitispringboot.util.SpringContextUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

public abstract class AbstractCountersignCmd {

    protected RuntimeService runtimeService;

    protected TaskService taskService;

    protected RepositoryService repositoryService;

    public AbstractCountersignCmd() {

        runtimeService = (RuntimeService) SpringContextUtils.getBeanByClass(RuntimeService.class);
        taskService = (TaskService) SpringContextUtils.getBeanByClass(TaskService.class);
        repositoryService = (RepositoryService) SpringContextUtils.getBeanByClass(RepositoryService.class);
    }

}