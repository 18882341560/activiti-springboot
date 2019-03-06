package com.gelin.activitispringboot.actviti.mail;

import com.google.common.collect.Maps;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.Map;
import java.util.Objects;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/3/6/006
 * @description: 邮件
 */
public class MailListener implements ExecutionListener {

    private static final long serialVersionUID = -5229066367096748486L;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        String eventName = delegateExecution.getEventName();
        if(Objects.equals(eventName,"start")){
            System.out.println("邮件流程开始创建");
            Map<String,String> map = Maps.newHashMap();
            map.put("mailTo","gllovejava@163.com"); //收件人（必选）
            map.put("mailFrom",null); //发送人地址
            map.put("mailSubject","流程邮件测试"); //邮件主题
            map.put("mailCc",null); //抄送
            map.put("mailBcc",null); //密送
            map.put("mailCharset","UTF-8"); //邮件字符集，中文邮件
            map.put("mailHtml","<!DOCTYPE html>\n" +
                    "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<a href=\"http://www.baidu.com\">点我</a>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>"); //这种格式的正文
            map.put("mailText",null);

            delegateExecution.setVariables(map);
        }

        if(Objects.equals(eventName,"end")){
            System.out.println("邮件任务结束");
        }

    }
}
