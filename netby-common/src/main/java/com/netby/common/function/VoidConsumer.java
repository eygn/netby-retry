package com.netby.common.function;

/**
 * @author: byg
 */
@FunctionalInterface
public interface VoidConsumer {
    /**
     * 消费
     * @throws Exception e
     */
    void accept() throws Exception;
}
