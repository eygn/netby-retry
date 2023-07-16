package com.netby.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Boot Starter
 *
 * @author Frank Zhang
 */
@EnableFeignClients(basePackages = {"com.netby"})
@SpringBootApplication(scanBasePackages = {"com.netby", "com.alibaba.cola"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
