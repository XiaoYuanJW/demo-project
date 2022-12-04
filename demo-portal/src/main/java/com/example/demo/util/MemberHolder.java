package com.example.demo.util;

import com.example.demo.entity.UmsMember;

/**
 * 线程变量操作工具类
 * Created by YuanJW on 2022/12/4.
 */
public class MemberHolder {
    private static final ThreadLocal<UmsMember> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(UmsMember umsMember) {
        THREAD_LOCAL.set(umsMember);
    }

    public static UmsMember get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
