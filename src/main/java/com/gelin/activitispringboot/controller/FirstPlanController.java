package com.gelin.activitispringboot.controller;

import com.gelin.activitispringboot.model.FirstPlan;
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



}
