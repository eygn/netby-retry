package com.netby.common.function;


import static java.util.stream.Stream.of;

import java.util.function.Predicate;

/**
 * The utilities class for Java {@link Predicate}
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public interface Predicates {

    Predicate<?>[] EMPTY_ARRAY = new Predicate[0];

    /**
     * {@link Predicate} always return <code>true</code>
     *
     * @param <T> the type to test
     * @return <code>true</code>
     */
    static <T> Predicate<T> alwaysTrue() {
        return e -> true;
    }

    /**
     * {@link Predicate} always return <code>false</code>
     *
     * @param <T> the type to test
     * @return <code>false</code>
     */
    static <T> Predicate<T> alwaysFalse() {
        return e -> false;
    }

    /**
     * 与条件
     *
     * @param predicates {@link Predicate predicates}
     * @param <T>        the type to test
     * @return non-null
     */
    @SafeVarargs
    static <T> Predicate<T> and(Predicate<T>... predicates) {
        return of(predicates).reduce(Predicate::and).orElseGet(Predicates::alwaysTrue);
    }

    /**
     * 或条件
     *
     * @param predicates {@link Predicate predicates}
     * @param <T>        the detected type
     * @return non-null
     */
    @SafeVarargs
    static <T> Predicate<T> or(Predicate<T>... predicates) {
        return of(predicates).reduce(Predicate::or).orElse(e -> true);
    }

}
