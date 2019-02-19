package com.gelin.activitispringboot.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @Auther 葛林
 * @Date 2019/2/1/001 11
 * @Remarks   获取ApplicationContext和Object的工具类
 *
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext atx;

    public static ApplicationContext getAtx() {
        return atx;
    }

    public static void setAtx(ApplicationContext atx) {
        SpringContextUtils.atx = atx;
    }

    /**
     * 根据bean的class来查找对象
     * @param c
     * @return
     */
    public static Object getBeanByClass(Class c){
        return SpringContextUtils.atx.getBean(c);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.atx = applicationContext;
    }
}