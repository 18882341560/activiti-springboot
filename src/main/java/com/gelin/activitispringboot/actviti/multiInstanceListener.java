package com.gelin.activitispringboot.actviti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.Arrays;
import java.util.List;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/8/5
 * @description:
 */
public class multiInstanceListener implements ExecutionListener {
    private static final long serialVersionUID = -642708315294921408L;

    @Override
    public void notify(DelegateExecution execution) {
        List<String> list = Arrays.asList("王五", "李四");
        execution.setVariable("userList", list);
    }
}
