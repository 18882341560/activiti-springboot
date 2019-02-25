package com.gelin.activitispringboot.service;

import com.gelin.activitispringboot.model.FirstPlan;
import com.gelin.activitispringboot.model.User;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/20/020 18
 * @Remarks
 */
public interface FirstPlanService {
    Object save(FirstPlan firstPlan) throws Exception;
    //我的代办任务
    List<FirstPlan> myAgencyTask(Integer userId) throws Exception;
    Object applyFirstPlan(Integer id) throws Exception;
    void examHtml(Integer id,Model model) throws Exception;
    Object exam(String remarks, Integer type,Integer id,User user) throws Exception;
    Object arrange(FirstPlan firstPlan,Integer userId) throws Exception;
    void myFirstList(HttpSession session, Model model) throws Exception;
}
