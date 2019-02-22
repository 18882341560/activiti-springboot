package com.gelin.activitispringboot.controller;

import com.gelin.activitispringboot.dao.BaseDao;
import com.gelin.activitispringboot.model.FirstPlan;
import com.gelin.activitispringboot.model.FirstRecords;
import com.gelin.activitispringboot.model.User;
import com.gelin.activitispringboot.service.FirstPlanService;
import com.gelin.activitispringboot.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/20/020 18
 * @Remarks 首检计划
 */
@Controller
@RequestMapping("/first")
public class FirstPlanController {

    @Autowired
    private FirstPlanService firstPlanService;
    @Autowired
    private BaseDao baseDao;


    @RequestMapping("/addFirstHtml")
    public String addFirstHtml(){
        return "first/addFirst";
    }


    @RequestMapping("/save")
    @ResponseBody
    public Object save(FirstPlan firstPlan, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        firstPlan.setCreateUserId(user.getId());
        firstPlan.setStatus(1);
        firstPlan.setCreateDateTime(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss());
        return firstPlanService.save(firstPlan);
    }


    @RequestMapping("/firstListHtml")
    public String firstListHtml(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        List<FirstPlan> list = firstPlanService.myAgencyTask(user.getId());
        model.addAttribute("list",list);
        return "first/firstPlanList";
    }


    //办理首检任务的申请
    @RequestMapping("/applyFirstPlan")
    @ResponseBody
    public Object applyFirstPlan(Integer id) throws Exception {
        return firstPlanService.applyFirstPlan(id);
    }

    @RequestMapping("/examHtml")
    public String examHtml(Integer id,Model model){
        FirstPlan firstPlan = baseDao.findOneFirstPlanById(id);
        List<FirstRecords> firstRecordList = baseDao.findFirstRecordList(id);
        model.addAttribute("first",firstPlan);
        model.addAttribute("list",firstRecordList);
        return "first/examFirst";
    }

    @RequestMapping("/exam")
    @ResponseBody
    public Object exam(String remarks,Integer type,Integer id,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return firstPlanService.exam(remarks,type,id,user);
    }

    @RequestMapping("/arrange")
    @ResponseBody
    public Object arrange(FirstPlan firstPlan,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return firstPlanService.arrange(firstPlan,user.getId());
    }

    @RequestMapping("/myFirstList")
    public String myFirstList(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        List<FirstPlan> list = baseDao.findFirstPlanByCreateUserId(user.getId());
        model.addAttribute("list",list);
        return "first/myFirstPlanList";
    }

}
