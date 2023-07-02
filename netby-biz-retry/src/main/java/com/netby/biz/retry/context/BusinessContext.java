package com.netby.biz.retry.context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;

import com.netby.biz.retry.constants.ContextConstants;
import com.netby.common.util.JsonUtil;
import com.netby.common.util.NumberUtil;
import com.netby.common.util.StringUtil;
import com.netby.common.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.env.Environment;

/**
 * 注意，非web应用入口使用的话一定要线程结束前，主动调用clear清除缓存；web应用会自动在ServletAttributes主动清除缓存
 *
 * @author: byg
 */
@Slf4j
@SuppressWarnings("unused")
public class BusinessContext {

    private static final LongAdder THREAD_COUNT = new LongAdder();

    private static final Integer SPAN_LIMIT = 2;

    private static String projectTag;
    private static List<String> otherTraceTags;

    private static final List<String> DEFAULT_TRACE_TAGS = ImmutableList.of(ContextConstants.ROUTE_ID, ContextConstants.ROUTE_TAG);

    private static final ThreadLocal<InnerContext> CONTEXT = new ThreadLocal<>();

    public static void addTheadCount() {
        if (CONTEXT.get() != null) {
            THREAD_COUNT.increment();
        }
    }

    public static void reduceTheadCount() {
        THREAD_COUNT.decrement();
    }

    public static long getLocalThreadCount() {
        return THREAD_COUNT.longValue();
    }

    private static InnerContext getContext(boolean force) {
        InnerContext inner = CONTEXT.get();
        if (force && inner == null) {
            inner = new InnerContext();
            CONTEXT.set(inner);
        }
        return inner;
    }

    private static boolean checkValue(Object value) {
        if (!Objects.isNull(value)) {
            if (value instanceof String) {
                return ((String) value).length() != 0;
            }
            return true;
        }
        return false;
    }

    public static void putRemote(String key, Object value) {
        if (checkValue(value)) {
            getContext(true).putRemote(key, value);
        }
    }

    public static void putSystem(String key, Object value) {
        if (checkValue(value)) {
            getContext(true).putSystem(key, value);
        }
    }

    public static void put(String key, Object value) {
        if (!Objects.isNull(value)) {
            if (value instanceof String) {
                if (((String) value).length() == 0) {
                    return;
                }
            }
            getContext(true).put(key, value);
        }
    }

    public static <T> T get(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return null;
        }
        return inner.get(key);
    }

    public static <T> T getSystem(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return null;
        }
        return inner.getSystem(key);
    }

    public static <T> T getRemote(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return null;
        }
        return inner.getRemote(key);
    }

    public static boolean contains(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return false;
        }
        return inner.has(key);
    }

    public static boolean hasSystem(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return false;
        }
        return inner.hasSystem(key);
    }

    public static void remove(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return;
        }
        inner.remove(key);
    }

    public static void removeSystem(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return;
        }
        inner.removeSystem(key);
    }

    public static void removeRemote(String key) {
        val inner = getContext(false);
        if (inner == null) {
            return;
        }
        inner.removeRemote(key);
    }

    public static boolean isEmpty() {
        return CONTEXT.get() == null || CONTEXT.get().isSystemEmpty();
    }

    public static InnerContext getAll() {
        return getContext(true);
    }

    public static void clear() {
        if (CONTEXT.get() != null) {
            reduceTheadCount();
        }
        removeLocalThread();
    }

    /**
     * 请注意只用于声明线程池清除
     */
    public static void removeLocalThread() {
        if (log.isDebugEnabled()) {
            log.debug("clear:{},id:{}", JsonUtil.writeValueAsString(CONTEXT.get()), Thread.currentThread().getId());
        }
        val inner = CONTEXT.get();
        if (inner != null) {
            inner.clear();
        }
        CONTEXT.remove();
    }

    public static Map<String, Object> getAllRemote() {
        val inner = getContext(false);
        if (inner == null) {
            return null;
        }
        return inner.getRemoteMap();
    }

    public static void reset(InnerContext context) {
        getContext(true).putAll(context);
        refreshTracing(null, null, newThreadSpanId(get(ContextConstants.ROUTE_SPAN_ID)));
    }

    /**
     * 刷新span，这里不会主动添加上下文
     */
    public static void refreshTracingSpan() {
        if (isEmpty()) {
            return;
        }
        refreshTracing(null, null, null);
    }

    /**
     * 刷新跟踪链
     */
    public static void refreshTracing() {
        refreshTracing(null, null, null);
        addTheadCount();
    }

    public static void refreshTracing(String routeId, String tag, String spanId) {
        if (StringUtil.isNotEmpty(routeId)) {
            putSystem(ContextConstants.ROUTE_ID, routeId);
        } else if (!hasSystem(ContextConstants.ROUTE_ID)) {
            putSystem(ContextConstants.ROUTE_ID, IDGenerator.uuid());
        }

        if (StringUtil.isNotEmpty(tag)) {
            putSystem(ContextConstants.ROUTE_TAG, tag);
        } else {
            if (tag != null) {
                removeSystem(ContextConstants.ROUTE_TAG);
            } else {
                if (!hasSystem(ContextConstants.ROUTE_TAG)) {
                    tag = projectTag;
                    if (StringUtil.isNotEmpty(tag)) {
                        putSystem(ContextConstants.ROUTE_TAG, tag);
                    }
                }
            }
        }

        if (hasSystem(ContextConstants.ROUTE_SPAN_ID)) {
            if (StringUtil.isNotEmpty(spanId)) {
                putSystem(ContextConstants.ROUTE_SPAN_ID, spanId);
            } else {
                putSystem(ContextConstants.ROUTE_SPAN_ID, addSpanId(getSystem(ContextConstants.ROUTE_SPAN_ID)));
            }
        } else {
            if (StringUtil.isNotEmpty(spanId)) {
                putSystem(ContextConstants.ROUTE_SPAN_ID, spanId);
            } else {
                putSystem(ContextConstants.ROUTE_SPAN_ID, addSpanId(null));
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("refresh trace:{},id:{}", JsonUtil.writeValueAsString(CONTEXT.get()), Thread.currentThread().getId());
        }
    }

    public static void refreshTracing(String routeTag) {
        refreshTracing(null, routeTag, null);
    }

    public static void applyContext(BiConsumer<String, Object> consumer) {
        val inner = getContext(false);
        if (inner == null) {
            return;
        }
        inner.getSystemMap().forEach((key, value) -> {
            if (value != null && StringUtil.isNotEmpty(value.toString())) {
                consumer.accept(key, value);
            }
        });
    }

    private static String addSpanId(String spanId) {
        if (StringUtil.isEmpty(spanId)) {
            return "1-1";
        }
        String[] arr = spanId.split("-", 2);
        if (arr.length != SPAN_LIMIT) {
            return spanId + "-1";
        }
        return (NumberUtil.parseLong(arr[0], 1L) + 1) + "-" + arr[1];
    }

    private static String newThreadSpanId(String spanId) {
        if (StringUtil.isEmpty(spanId)) {
            return "1-1";
        }
        String[] arr = spanId.split("-", 2);
        if (arr.length != SPAN_LIMIT) {
            return spanId + "-1";
        }
        return (NumberUtil.parseLong(arr[0], 1L) + 1) + "-" + (NumberUtil.parseLong(arr[1], 1L) + 1);
    }

    /**
     * 得到当期项目的tag,兼容MSE
     *
     * @param env env
     * @return tag
     */
    public static String getSystemTag(Environment env) {
        String tag = env.getProperty("netby.cloud.tag");
        if (StringUtil.isEmpty(tag)) {
            tag = env.getProperty("alicloud.service.tag");
        }
        return tag;
    }

    /**
     * 得到当期项目的tag,兼容MSE
     *
     * @return tag
     */
    public static String getProjectTag() {
        return projectTag;
    }

    public static void setEnvironment(Environment env) {
        projectTag = getSystemTag(env);
        String tags = env.getProperty("netby.context.other-trace-tags");
        if (StringUtil.isNotEmpty(tags)) {
            otherTraceTags = Lists.newArrayList(tags.split(","));
        }
    }

    private static void addValue(String key, StringBuilder builder) {
        Object obj = getSystem(key);
        String value = null;
        if (obj != null) {
            value = obj.toString();
        }
        if (builder.length() > 0) {
            builder.append(":");
        }
        if (StringUtil.isNotEmpty(value)) {
            builder.append(value);
        }
    }

    public static String getLogTrace() {
        StringBuilder builder = new StringBuilder();
        for (String key : DEFAULT_TRACE_TAGS) {
            addValue(key, builder);
        }
        if (otherTraceTags != null) {
            for (String key : otherTraceTags) {
                addValue(key, builder);
            }
        }
        return builder.toString();
    }

    public static List<String> getOtherTraceTags() {
        return otherTraceTags;
    }
}
