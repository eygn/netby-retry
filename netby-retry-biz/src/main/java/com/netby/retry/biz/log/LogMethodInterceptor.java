package com.netby.retry.biz.log;

import com.netby.retry.biz.annotation.LogPrinter;
import com.netby.common.util.CollectionUtils;
import com.netby.common.util.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: byg
 */
@Slf4j
public class LogMethodInterceptor implements MethodInterceptor {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    @Setter
    private LogPrinterFunction logPrinterFunction;
    /**
     * 分割字符串缓存，追求性能极致
     */
    private final Map<String, String[]> splitCache = new ConcurrentHashMap<>();

    private String ignoreText = "@忽略@";
    private final Set<String> globalIgnoreReq = new HashSet<>();
    private final Set<String> globalIgnoreResp = new HashSet<>();

    public void setIgnoreText(String ignoreText) {
        if (StringUtil.isNotBlank(ignoreText)) {
            this.ignoreText = ignoreText;
        }
    }

    public void addGlobalIgnoreReq(Collection<String> ignores) {
        if (!CollectionUtils.isEmpty(ignores)) {
            globalIgnoreReq.addAll(ignores);
            log.info("配置全局忽略请求字段[{}]", String.join(",", ignores));
        }
    }

    public void addGlobalIgnoreResp(Collection<String> ignores) {
        if (!CollectionUtils.isEmpty(ignores)) {
            globalIgnoreResp.addAll(ignores);
            log.info("配置全局忽略返回字段[{}]", String.join(",", ignores));
        }
    }

    private String getResponseLog(Set<String> ignoreResp, Object result) {
        if (result == null) {
            return null;
        }
        if (isSimpleType(result)) {
            return result.toString();
        }
        // 集合和数组处理
        if (result instanceof Collection) {
            return "[" + ((Collection<?>) result).stream().map(o -> getResponseLog(ignoreResp, o)).collect(Collectors.joining(",")) + "]";
        } else if (result.getClass().isArray()) {
            return "[" + Stream.of((Object[]) result).map(o -> getResponseLog(ignoreResp, o)).collect(Collectors.joining(",")) + "]";
        }
        Map<String, Object> map = logPrinterFunction.convert2Map(result);
        for (String ignore : ignoreResp) {
            removeIgnoreArgs(map, splitIgnore(ignore));
        }
        return logPrinterFunction.toString(map);
    }

    private boolean isSimpleType(Object result) {
        Class<?> clazz = result.getClass();
        // 是否字符
        return CharSequence.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                // 是否布尔
                || Boolean.class.isAssignableFrom(clazz)
                // 是否数字
                || Number.class.isAssignableFrom(clazz)
                // 是否时间
                || Date.class.isAssignableFrom(clazz)
                || Temporal.class.isAssignableFrom(clazz);
    }

    private Map<String, Object> getRequestLog(Set<String> ignoreReq, String[] parameters, Object[] args) {
        Map<String, Object> argsMap = new HashMap<>(args.length);
        if (parameters == null) {
            return new HashMap<>(0);
        }
        for (int i = 0; i < parameters.length; i++) {
            argsMap.put(parameters[i], args[i]);
        }
        Map<String, Object> map = logPrinterFunction.convert2Map(argsMap);
        for (String ignore : ignoreReq) {
            removeIgnoreArgs(map, splitIgnore(ignore));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void removeIgnoreArgs(Map<String, Object> map, String[] ignore) {
        if (ignore == null || ignore.length == 0 || !map.containsKey(ignore[0])) {
            return;
        }
        if (ignore.length == 1) {
            map.put(ignore[0], ignoreText);
        } else {
            Map<String, Object> childValue = (Map<String, Object>) map.compute(
                    ignore[0],
                    (key, old) -> old instanceof Map ? old : new HashMap<>(0)
            );
            removeIgnoreArgs(childValue, splitIgnore(ignore[1]));
        }
    }

    /**
     * 因为忽略的字段是有限的，所以可以做一层缓存，尽可能提升性能
     */
    private String[] splitIgnore(String ignore) {
        return splitCache.computeIfAbsent(ignore, s -> StringUtil.split(ignore, ".", 2));
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object target = methodInvocation.getThis();
        Object[] args = methodInvocation.getArguments();
        // 处理类上的配置
        assert target != null;
        LogPrinter classLogPointer = target.getClass().getAnnotation(LogPrinter.class);
        boolean classEnable = true;
        boolean methodEnable = true;
        Set<String> ignoreReq = new HashSet<>(globalIgnoreReq);
        Set<String> ignoreResp = new HashSet<>(globalIgnoreResp);
        if (classLogPointer != null) {
            classEnable = classLogPointer.enable();
            ignoreReq.addAll(Stream.of(classLogPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(classLogPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        // 处理方法上的配置
        LogPrinter methodLogPointer = method.getAnnotation(LogPrinter.class);
        if (methodLogPointer != null) {
            methodEnable = methodLogPointer.enable();
            ignoreReq.addAll(Stream.of(methodLogPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(methodLogPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        String name = target.getClass().getName() + "#" + method.getName();
        Logger printLogger = LoggerFactory.getLogger(name);
        boolean enable = classEnable && methodEnable;
        if (enable) {
            try {
                // 入参打印
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                Map<String, Object> requestLog = getRequestLog(ignoreReq, parameterNames, args);
                printLogger.info("入参：[{}]", logPrinterFunction.toString(requestLog));
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        // 真正处理
        Object result = methodInvocation.proceed();
        if (enable) {
            try {
                // 出参打印
                String responseLog = getResponseLog(ignoreResp, result);
                printLogger.info("出参：[{}]", responseLog);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }

        }
        return result;
    }
}
