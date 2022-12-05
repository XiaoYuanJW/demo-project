package com.example.demo.util;

import com.example.demo.dto.MemberDto;

/**
 * 线程变量操作工具类
 * Created by YuanJW on 2022/12/4.
 */
public class MemberHolder {
    private static final ThreadLocal<MemberDto> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(MemberDto memberDto) {
        THREAD_LOCAL.set(memberDto);
    }

    public static MemberDto get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
