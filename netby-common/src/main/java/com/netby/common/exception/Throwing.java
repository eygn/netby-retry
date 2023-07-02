package com.netby.common.exception;

import java.util.function.Consumer;
import java.util.function.Function;

import com.netby.common.function.ExConsumer;
import com.netby.common.function.ExFunction;
import lombok.NonNull;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class Throwing {
    private Throwing() {
    }

    @NonNull
    public static <T> Consumer<T> rethrow(@NonNull final ExConsumer<T> consumer) {
        return consumer;
    }

    @NonNull
    public static <T, R> Function<T, R> rethrow(@NonNull final ExFunction<T, R> function) {
        return function;
    }

    /**
     * The compiler sees the signature with the throws T inferred to a RuntimeException type, so it allows the unchecked
     * exception to propagate.
     * <p>
     * <a href="http://www.baeldung.com/java-sneaky-throws">link</a>
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(@NonNull final Throwable ex) throws E {
        throw (E) ex;
    }
}
