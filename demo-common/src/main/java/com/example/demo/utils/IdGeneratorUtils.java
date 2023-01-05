package com.example.demo.utils;

import com.example.demo.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Id生成策略工具类
 * Created by YuanJW on 2022/12/9.
 */
@Component
public class IdGeneratorUtils {
    @Resource
    private RedisService redisService;
    /**
     * 开始时间戳（2022-1-1 00:00:00）
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 位运算
     */
    private static final int OFFSET = 32;

    /**
     * 键的前缀
     */
    private static final String KEY_PREFIX = "id";

    public long generateId(String key) {
        // 生成时间戳
        long stamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMESTAMP;
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 生成序列号
        long incr = redisService.incr(KEY_PREFIX + ":" + key + ":" + date, 1L);
        // 返回id
        return stamp << OFFSET | incr;
    }
}
