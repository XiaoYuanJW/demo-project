package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 应用程序入口
 * Created by YuanJW on 2022/12/2.
 */
@EnableAsync
@SpringBootApplication
public class DemoPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoPortalApplication.class, args);
    }
}
