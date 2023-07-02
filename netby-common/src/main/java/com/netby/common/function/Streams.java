package com.netby.common.function;

import static com.netby.common.function.Predicates.and;
import static com.netby.common.function.Predicates.or;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author: byg
 */
public interface Streams {

    /**
     * 过滤流
     *
     * @param values    值
     * @param predicate 条件
     * @return 流
     */
    static <T, S extends Iterable<T>> Stream<T> filterStream(S values, Predicate<T> predicate) {
        return stream(values.spliterator(), false).filter(predicate);
    }

    /**
     * 过滤List
     *
     * @param values    值
     * @param predicate 条件
     * @return 列表
     */
    static <T, S extends Iterable<T>> List<T> filterList(S values, Predicate<T> predicate) {
        return filterStream(values, predicate).collect(toList());
    }

    /**
     * 过滤Set
     *
     * @param values    值
     * @param predicate 条件
     * @return Set
     */
    static <T, S extends Iterable<T>> Set<T> filterSet(S values, Predicate<T> predicate) {
        // new Set with insertion order
        return filterStream(values, predicate).collect(LinkedHashSet::new, Set::add, Set::addAll);
    }

    /**
     * 过滤流
     *
     * @param values    值
     * @param predicate 条件
     * @return 流
     */
    @SuppressWarnings("unchecked")
    static <T, S extends Iterable<T>> S filter(S values, Predicate<T> predicate) {
        final boolean isSet = Set.class.isAssignableFrom(values.getClass());
        return (S) (isSet ? filterSet(values, predicate) : filterList(values, predicate));
    }

    /**
     * 过滤流
     *
     * @param values     值
     * @param predicates 多个条件
     * @return 流
     */
    @SafeVarargs
    static <T, S extends Iterable<T>> S filterAll(S values, Predicate<T>... predicates) {
        return filter(values, and(predicates));
    }

    /**
     * 过滤流
     *
     * @param values     值
     * @param predicates 任一个条件
     * @return 流
     */
    @SafeVarargs
    static <T, S extends Iterable<T>> S filterAny(S values, Predicate<T>... predicates) {
        return filter(values, or(predicates));
    }

    /**
     * 过滤流
     *
     * @param values     值
     * @param predicates 任一个条件
     * @return 第一条
     */
    @SafeVarargs
    static <T> T filterFirst(Iterable<T> values, Predicate<T>... predicates) {
        return stream(values.spliterator(), false)
            .filter(and(predicates))
            .findFirst()
            .orElse(null);
    }
}