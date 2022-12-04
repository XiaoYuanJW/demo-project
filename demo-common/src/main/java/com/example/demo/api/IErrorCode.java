package com.example.demo.api;

/**
 * 常见API返回对象接口
 * Created by YuanJW on 2022/6/29.
 */
public interface IErrorCode {
    /**
     * 获取返回码
     * @return
     */
    long getCode();
    /**
     * 获取返回码
     * @return
     */
    String getMessage();
}
