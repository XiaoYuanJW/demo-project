package com.example.demo.constants;

import java.util.Collections;
import java.util.Map;

/**
 * Redis接口常量
 * Created by YuanJW on 2022/12/7.
 */
public class RedisConstant {
    public static final String NULL_VALUE_KEY = "null";

    public static final String NULL_VALUE = "null";

    // 防止缓存穿透的空对象
    public final static Map<String, Object> NULL_MAP = Collections.singletonMap(NULL_VALUE_KEY, null);
}
