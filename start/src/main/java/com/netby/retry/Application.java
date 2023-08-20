package com.netby.retry;

import com.netby.retry.domain.retry.annotation.EnableSyncRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Boot Starter
 *
 * @author Elvan.bai
 */
@EnableSyncRetry
@EnableFeignClients(basePackages = {"com.netby.retry.api"})
@SpringBootApplication(scanBasePackages = {"com.netby", "com.alibaba.cola"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
