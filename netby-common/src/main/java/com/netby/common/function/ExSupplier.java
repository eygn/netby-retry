package com.netby.common.function;


import com.netby.common.exception.Throwing;

import java.util.function.Supplier;

/**
 * @author: byg
 * @since 2021/8/7 18:34
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface ExSupplier<R> extends Supplier<R> {

    /**
     * 获取
     *
     * @return 输出
     */
    @Override
    default R get() {
        try {
            return innerGet();
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
        return null;
    }

    /**
     * 内部获取
     *
     * @return 输出
     * @throws Throwable ex
     */
    R innerGet() throws Throwable;
}
