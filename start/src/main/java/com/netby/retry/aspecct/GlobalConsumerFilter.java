package com.netby.retry.aspecct;

import com.netby.common.util.JsonUtil;
import com.netby.common.vo.Response;
import com.netby.core.mock.HttpFastMock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 全局消费外调接口统一拦截器
 *
 * @author baiyg
 * @date 2023/6/2 12:51
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GlobalConsumerFilter {

    @Pointcut("execution (* com.netby.retry.integration..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object timedMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = pjp.proceed();
            return result;
        } finally {
            try {
                String signatureName = pjp.getSignature().toShortString();
                if (!signatureName.toLowerCase().contains("securityhandle") && !signatureName.toLowerCase().contains("riskapiconfig")) {
                    log.info("[外调接口]cost:{},method:{},params:{},result:{}", System.currentTimeMillis() - startTime, signatureName, JsonUtil.writeValueAsString(args), JsonUtil.writeValueAsString(result));
                }
                if (result instanceof Response && ((Response) result).isFailed()) {
                    if (((Response) result).isFailed()) {
                        Response response = HttpFastMock.mockResponse(signatureName);
                        if (response != null && response.isSuccess()) {
                            return response;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("timedMethod exception", e);
            }
        }
    }


}
