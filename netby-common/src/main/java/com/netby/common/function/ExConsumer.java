package com.netby.common.function;


import com.netby.common.exception.Throwing;

import java.util.function.Consumer;

/**
 * @author: byg
 */
@FunctionalInterface
public interface ExConsumer<T> extends Consumer<T> {

    /**
     * 消费
     *
     * @param e 数据
     */
    @Override
    default void accept(final T e) {
        try {
            innerAccept(e);
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
    }

    /**
     * 内部消费
     *
     * @param e 数据
     * @throws Throwable ex
     */
    void innerAccept(T e) throws Throwable;
}
