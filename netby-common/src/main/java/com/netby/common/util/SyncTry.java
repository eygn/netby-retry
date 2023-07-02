package com.netby.common.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 阻塞式判断
 *
 * @author: byg
 */
public class SyncTry {

    public static void test(Supplier<Boolean> supplier) {
        test(supplier, Duration.ofSeconds(1));
    }

    public static void test(Supplier<Boolean> supplier, Duration duration) {
        do {
            if (supplier.get()) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                break;
            }
        } while (true);
    }
}
