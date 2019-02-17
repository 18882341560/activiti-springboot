package com.gelin.activitispringboot.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create gl  2019/1/20
 **/
@RequestMapping("/index")
@Controller
public class IndexController {

    @Resource
    private RepositoryService repositoryService;


    @RequestMapping("/index")
    public String index(){
        return "index";
    }


    @RequestMapping("/deployList")
    public String deployList(Model model){


        List<ProcessDefinition> process = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        List<Deployment> list = new ArrayList<>();

        process.forEach( p ->{
            list.add(
                    repositoryService.createDeploymentQuery().deploymentId(p.getDeploymentId()).singleResult());
        });

        List<Map<String,Object>> mapList = new ArrayList<>();
        process.forEach( p ->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",p.getId());
            map.put("version",p.getVersion());
            map.put("name",p.getName());
            map.put("deploymentId",p.getDeploymentId());
            map.put("bpmnName",p.getResourceName());
            map.put("pngName",p.getDiagramResourceName());
            mapList.add(map);
        });

        List<Map<String,Object>> depList = new ArrayList<>();

        list.forEach( d ->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",d.getId());
            map.put("name",d.getName());
            map.put("deployTime",d.getDeploymentTime());
            depList.add(map);
        });


        model.addAttribute("processList",mapList);
        model.addAttribute("deploymentList",depList);
        return "depaymentList";
    }
}
