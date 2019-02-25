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


    /**
     * 添加首检申请页面
     * @return
     */
    @RequestMapping("/addFirstHtml")
    public String addFirstHtml(){
        return "first/addFirst";
    }


    /**
     * 新增申请
     * @param firstPlan
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object save(FirstPlan firstPlan, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        firstPlan.setCreateUserId(user.getId());
        firstPlan.setStatus(1);
        firstPlan.setCreateDateTime(DateUtils.getLocalDateTimeByYYYYMMDDHHmmss());
        return firstPlanService.save(firstPlan);
    }


    /**
     * 我的首检任务列表办理页面
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/firstListHtml")
    public String firstListHtml(Model model,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        List<FirstPlan> list = firstPlanService.myAgencyTask(user.getId());
        model.addAttribute("list",list);
        return "first/firstPlanList";
    }


    /**
     * 办理首检任务的申请
     * @param id 首检id
     * @return
     * @throws Exception
     */
    @RequestMapping("/applyFirstPlan")
    @ResponseBody
    public Object applyFirstPlan(Integer id) throws Exception {
        return firstPlanService.applyFirstPlan(id);
    }

    /**
     * 审核页面
     * @param id 首检id
     * @param model
     * @return
     */
    @RequestMapping("/examHtml")
    public String examHtml(Integer id,Model model) throws Exception {
        firstPlanService.examHtml(id,model);
        return "first/examFirst";
    }

    /**
     * 审核
     * @param remarks   意见
     * @param type   审核类型 1 通过 2 驳回
     * @param id     首检id
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/exam")
    @ResponseBody
    public Object exam(String remarks,Integer type,Integer id,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return firstPlanService.exam(remarks,type,id,user);
    }

    /**
     * 安排首检时间
     * @param firstPlan
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/arrange")
    @ResponseBody
    public Object arrange(FirstPlan firstPlan,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        return firstPlanService.arrange(firstPlan,user.getId());
    }

    /**
     * 我的首检申请列表
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/myFirstList")
    public String myFirstList(HttpSession session,Model model) throws Exception {
         firstPlanService.myFirstList(session,model);
        return "first/myFirstPlanList";
    }

}
