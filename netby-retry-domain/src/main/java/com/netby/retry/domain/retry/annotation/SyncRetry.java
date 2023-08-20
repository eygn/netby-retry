package com.netby.retry.domain.retry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Elvan.bai
 * @date: 2023/8/19 09:38
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncRetry {

    /**
     * 重试标签说明
     */
    String label() default "";

    /**
     * 最大重试次数，默认为3
     */
    int maxAttempts() default 3;
}
