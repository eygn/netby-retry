package com.netby.common.util;

import com.netby.common.function.VoidConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author: byg
 */
@Slf4j
public class StopWatchUtils {
    /**
     * 耗时日志记录
     *
     * @param processor     Supplier
     * @param messageFormat 日志格式，参数 long time
     * @param <T>           结果类型
     * @return 结果
     */
    public static <T> T timeLog(Supplier<T> processor, String messageFormat) {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            return processor.get();
        } finally {
            log.info(String.format(messageFormat, stopWatch.getTime(TimeUnit.MILLISECONDS)));
        }
    }


    /**
     * 耗时日志记录
     *
     * @param processor     VoidConsumer
     * @param messageFormat 日志格式，参数 long time
     */
    @SneakyThrows
    public static void timeLog(VoidConsumer processor, String messageFormat) {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            processor.accept();
        } finally {
            log.info(String.format(messageFormat, stopWatch.getTime(TimeUnit.MILLISECONDS)));
        }
    }

    /**
     * 耗时拦截
     *
     * @param processor Supplier
     * @param time      耗时回调
     * @param <T>       结果
     * @return 结果
     */
    public static <T> T time(Supplier<T> processor, Consumer<Long> time) {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            return processor.get();
        } finally {
            time.accept(stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 耗时日志记录
     *
     * @param processor 处理过程
     * @param time      通过耗时获取日志
     * @param <T>       结果
     * @return 结果
     */
    public static <T> T timeLog(Supplier<T> processor, Function<Long, String> time) {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            return processor.get();
        } finally {
            log.info(time.apply(stopWatch.getTime(TimeUnit.MILLISECONDS)));
        }
    }
}
