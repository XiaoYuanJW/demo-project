package com.example.demo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring工具类
 * Created by YuanJW on 2022/12/5.
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置applicationContext
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 通过name获取Bean
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class clazz) {
        return (T) getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name和class获取Bean
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name, Class clazz) {
        return (T) getApplicationContext().getBean(name, clazz);
    }
}
