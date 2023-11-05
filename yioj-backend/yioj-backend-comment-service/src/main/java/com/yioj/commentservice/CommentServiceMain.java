package com.yioj.commentservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.yioj.commentservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.yioj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yioj.clientservice.service"})
public class CommentServiceMain {
    public static void main(String[] args) {
        // InitRabbitMq.doInit();
        SpringApplication.run(CommentServiceMain.class, args);
    }
}