package com.netby.retry.domain.retry.annotation;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.netby.core.context.SpringContextHolder;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.util.StringUtils;

/**
 * @author: Elvan.bai
 * @date: 2023/8/19 10:23
 */
@Slf4j
@Getter
@Setter
public class SyncRetryOperationsInterceptor implements MethodInterceptor {

    private String label;

    private int maxAttempts;


    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        String name;
        if (StringUtils.hasText(this.label)) {
            name = this.label;
        } else {
            name = invocation.getMethod().toGenericString();
        }
        try {
            return ((ProxyMethodInvocation) invocation).invocableClone().proceed();
        } catch (Throwable throwable) {
            log.error("执行异常,保存到异步重试,label:{}", name);
            BizRetryGateway retryGateway = SpringContextHolder.getBean(BizRetryGateway.class);
            BizRetry bizRetry = new BizRetry();
            bizRetry.setBizNo(UuidUtils.generateUuid());
            bizRetry.setRetryType("test");
            bizRetry.setBizType(name);
            bizRetry.setComment(throwable.getMessage());
            retryGateway.add(bizRetry);
            throw throwable;
        }
    }
}
