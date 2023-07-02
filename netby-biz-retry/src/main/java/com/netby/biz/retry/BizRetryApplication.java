package com.netby.biz.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: byg
 * @date: 2023/6/17 17:04
 */
@SpringBootApplication(scanBasePackages = {"com.netby"})
public class BizRetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizRetryApplication.class, args);
    }
}