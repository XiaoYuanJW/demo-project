package com.example.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 * Created by YuanJW on 2022/9/23.
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.example.demo.mapper")
public class MyBatisConfig {
}
