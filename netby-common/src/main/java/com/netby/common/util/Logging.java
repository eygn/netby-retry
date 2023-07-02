package com.netby.common.util;

import com.netby.common.function.VoidConsumer;
import lombok.SneakyThrows;
import org.slf4j.Logger;


/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class Logging {
    @SneakyThrows(Exception.class)
    public static void debug(Logger logger, VoidConsumer consumer) {
        if (logger.isDebugEnabled()) {
            consumer.accept();
        }
    }
    @SneakyThrows(Exception.class)
    public static void info(Logger logger, VoidConsumer consumer) {
        if (logger.isInfoEnabled()) {
            consumer.accept();
        }
    }
    @SneakyThrows(Exception.class)
    public static void warn(Logger logger, VoidConsumer consumer) {
        if (logger.isWarnEnabled()) {
            consumer.accept();
        }
    }
    @SneakyThrows(Exception.class)
    public static void error(Logger logger, VoidConsumer consumer) {
        if (logger.isErrorEnabled()) {
            consumer.accept();
        }
    }
    @SneakyThrows(Exception.class)
    public static void trace(Logger logger, VoidConsumer consumer) {
        if (logger.isTraceEnabled()) {
            consumer.accept();
        }
    }
}
