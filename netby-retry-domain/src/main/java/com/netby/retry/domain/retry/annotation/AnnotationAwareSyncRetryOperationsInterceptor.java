package com.netby.retry.domain.retry.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: Elvan.bai
 * @date: 2023/8/19 09:56
 */
public class AnnotationAwareSyncRetryOperationsInterceptor implements IntroductionInterceptor, BeanFactoryAware {

    private BeanFactory beanFactory;

    private static final MethodInterceptor NULL_INTERCEPTOR = methodInvocation -> {
        throw new OperationNotSupportedException("Not supported");
    };

    private final ConcurrentReferenceHashMap<Object, ConcurrentMap<Method, MethodInterceptor>> delegates = new ConcurrentReferenceHashMap<>();


    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        MethodInterceptor delegate = getDelegate(invocation.getThis(), invocation.getMethod());
        if (delegate != null) {
            return delegate.invoke(invocation);
        } else {
            return invocation.proceed();
        }
    }

    private MethodInterceptor getDelegate(Object target, Method method) {
        ConcurrentMap<Method, MethodInterceptor> cachedMethods = this.delegates.get(target);
        if (cachedMethods == null) {
            cachedMethods = new ConcurrentHashMap<>();
        }
        MethodInterceptor delegate = cachedMethods.get(method);

        if (delegate == null) {
            MethodInterceptor interceptor = NULL_INTERCEPTOR;
            SyncRetry retryable = AnnotatedElementUtils.findMergedAnnotation(method, SyncRetry.class);
            if (retryable != null) {
                interceptor = getInterceptor(target, method, retryable);
            }
            cachedMethods.putIfAbsent(method, interceptor);
            delegate = cachedMethods.get(method);
        }
        this.delegates.putIfAbsent(target, cachedMethods);
        return delegate == NULL_INTERCEPTOR ? null : delegate;
    }

    private MethodInterceptor getInterceptor(Object target, Method method, SyncRetry syncRetry) {
        SyncRetryOperationsInterceptor syncRetryOperationsInterceptor = new SyncRetryOperationsInterceptor();
        syncRetryOperationsInterceptor.setLabel(syncRetry.label());
        syncRetryOperationsInterceptor.setMaxAttempts(syncRetry.maxAttempts());
        return syncRetryOperationsInterceptor;

    }

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return SyncRetry.class.isAssignableFrom(intf);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
