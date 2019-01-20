package com.gelin.activitispringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * create gl  2019/1/20
 **/
@RequestMapping("/index")
@Controller
public class IndexController {


    @RequestMapping("/index")
    public String index(){
        return "index";
    }


}
