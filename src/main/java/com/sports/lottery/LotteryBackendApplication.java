package com.sports.lottery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 体彩投注记录管理系统后端应用启动类
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.sports.lottery.mapper")
@EnableScheduling
public class LotteryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryBackendApplication.class, args);
        System.out.println("=================================");
        System.out.println("体彩投注记录管理系统后端启动成功！");
        System.out.println("API文档地址: http://localhost:8080/swagger-ui.html");
        System.out.println("=================================");
    }
}