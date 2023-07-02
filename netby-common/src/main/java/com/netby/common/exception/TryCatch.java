package com.netby.common.exception;

import com.netby.common.function.VoidConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
@Slf4j
public class TryCatch {
    /**
     * 有时候为了防止程序出现异常而终止，我们不得不将代码用 try cache 包装起来，然后忽略 Exception
     * 该方法提供一个包装，简化代码
     *
     * @param consumer 实际的逻辑
     */
    public static void ignore(VoidConsumer consumer) {
        ignore(consumer, false);
    }

    public static void ignore(VoidConsumer consumer, boolean errorLog) {
        try {
            consumer.accept();
        } catch (Exception e) {
            if (errorLog) {
                log.error("error", e);
            }
        }
    }
}
