package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 自定义缓存异常注解，存在该注解的缓存方法会抛出异常
 * Created by YuanJW on 2022/12/5.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheException {
}
