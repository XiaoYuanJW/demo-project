package com.example.demo;

import com.example.demo.mapper.SmsCouponHistoryMapper;
import com.example.demo.utils.IdGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试类
 * Created by YuanJW on 2022/12/9.
 */
@Slf4j
@SpringBootTest
public class DemoPortalApplicantionTests {
    @Resource
    private SmsCouponHistoryMapper smsCouponHistoryMapper;
    @Resource
    private IdGeneratorUtils idGeneratorUtils;
    @Value("${redis.key.couponHistory}")
    private String REDIS_KEY_COUPON_HISTORY;

    private ExecutorService executorService = Executors.newFixedThreadPool(500);

    @Test
    public void contextLoads() throws InterruptedException {
        // 线程同步执行计数器
        CountDownLatch countDownLatch = new CountDownLatch(300);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    Long id = idGeneratorUtils.generateId("test");
                    log.info("id = " + id);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        log.info("time = " + (endTime - startTime));
    }
}
