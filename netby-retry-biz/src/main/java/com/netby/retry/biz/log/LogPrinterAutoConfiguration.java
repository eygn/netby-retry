package com.netby.retry.biz.log;

import com.netby.retry.biz.log.sensitive.LogSensitiveProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ResourceLoader;

/**
 * 日志打印自动配置类
 *
 * @author: byg
 */
@Slf4j
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({LogPrinterProperties.class, LogSensitiveProperties.class})
public class LogPrinterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "logPointcutAdvisor")
    @ConditionalOnProperty(name = "netby.log.enable", havingValue = "true")
    public DefaultPointcutAdvisor logPointcutAdvisor(LogPrinterProperties logPrinterProperties, ResourceLoader resourceLoader,
        LogPrinterFunction logPrinterFunction) {
        log.info("启用日志拦截功能.");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        LogMatchingPointcut logMatchingPointcut = new LogMatchingPointcut(logPrinterProperties.getPackagePath(), resourceLoader);
        advisor.setPointcut(logMatchingPointcut);
        Advice advice = getAdvice(logPrinterProperties, logPrinterFunction);
        advisor.setAdvice(advice);
        return advisor;
    }

    private Advice getAdvice(LogPrinterProperties logPrinterProperties, LogPrinterFunction logPrinterFunction) {
        LogMethodInterceptor advice = new LogMethodInterceptor();
        advice.setIgnoreText(logPrinterProperties.getIgnoreText());
        advice.addGlobalIgnoreReq(logPrinterProperties.getIgnoreReq());
        advice.addGlobalIgnoreResp(logPrinterProperties.getIgnoreResp());
        advice.setLogPrinterFunction(logPrinterFunction);
        return advice;
    }

    @Bean
    @ConditionalOnMissingBean(LogPrinterFunction.class)
    public LogPrinterFunction defaultLogPrinter() {
        return new DefaultLogPrinter();
    }
}
