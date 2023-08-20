package com.netby.retry.domain.retry.annotation;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author: Elvan.bai
 * @date: 2023/8/20 10:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(RetryConfiguration.class)
@Documented
public @interface EnableSyncRetry {

    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed to
     * standard Java interface-based proxies. The default is {@code false}.
     * @return whether to proxy or not to proxy the class
     */
    @AliasFor(annotation = EnableAspectJAutoProxy.class)
    boolean proxyTargetClass() default false;

    /**
     * Indicate the order in which the {@link RetryConfiguration} AOP <b>advice</b> should
     * be applied.
     * <p>
     * The default is {@code Ordered.LOWEST_PRECEDENCE - 1} in order to make sure the
     * advice is applied before other advices with {@link Ordered#LOWEST_PRECEDENCE} order
     * (e.g. an advice responsible for {@code @Transactional} behavior).
     */
    int order() default Ordered.LOWEST_PRECEDENCE - 1;
}
