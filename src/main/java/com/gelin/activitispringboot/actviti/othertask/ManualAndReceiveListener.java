package com.gelin.activitispringboot.actviti.othertask;

import com.google.common.collect.Maps;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.Map;


/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/6/006
 * @description:
 */
public class ManualAndReceiveListener implements ExecutionListener {


    private static final long serialVersionUID = -6354951643794203648L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String phone = (String)delegateExecution.getVariable("phone");
        System.out.println("给这个电话号码:"+phone+"发送短信");

//        Map<String,String> map = Maps.newHashMap();
//        map.put("mailTo","gllovejava@163.com"); //收件人（必选）
//        map.put("mailFrom",null); //发送人地址
//        map.put("mailSubject","流程邮件测试"); //邮件主题
//        map.put("mailCc",null); //抄送
//        map.put("mailBcc",null); //密送
//        map.put("mailCharset","UTF-8"); //邮件字符集，中文邮件
//        map.put("mailHtml","<!DOCTYPE html>\n" +
//                "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <title>Title</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "\n" +
//                "<a href=\"http://www.baidu.com\">点我</a>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>"); //这种格式的正文
//        map.put("mailText",null);
//        delegateExecution.setVariables(map);
    }
}
