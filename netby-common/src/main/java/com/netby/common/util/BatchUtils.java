package com.netby.common.util;

import com.netby.common.vo.optional.OptionalCollection;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 批量操作工具类
 *
 * @author: byg
 */
@Slf4j
@SuppressWarnings("unused")
public class BatchUtils {

    public static <T> long execute(int max, Collection<T> data, Consumer<OptionalCollection<T>> handler) {
        return execute(max, generator -> {
            for (T t : data) {
                generator.add(t);
            }
        }, handler);
    }

    /**
     * 批量操作
     * 通过 Generator#add 添加元素，每当元素达到 max 时，执行 handler 的方法，结束时没满 max 的集合需要再执行一次 handler 方法
     *
     * @param max       每批次最大数量
     * @param generator 生成对象
     * @param handler   满 max 时的操作
     * @param <T>       对象类型
     * @return 总数量
     */
    public static <T> long execute(int max, Consumer<Generator<T>> generator, Consumer<OptionalCollection<T>> handler) {
        final Generator<T> g = new Generator<>(max, handler);
        generator.accept(g);
        g.end();
        return g.count;
    }

    public static long execute(int max, Consumer<ExGenerator> generator, Function<Object, Object> signFunction, Map<Predicate<Object>, Consumer<OptionalCollection<Object>>> consumerMap) {
        final ExGenerator g = new ExGenerator(max, consumerMap, signFunction);
        generator.accept(g);
        g.end();
        return g.count;
    }

    @SuppressWarnings("unused")
    public static class Factory {
        private final Map<Predicate<Object>, Consumer<OptionalCollection<Object>>> consumerMap;

        private Factory() {
            consumerMap = new LinkedHashMap<>();
        }

        public static Factory builder() {
            return new Factory();
        }

        public Factory add(Predicate<Object> predicate, Consumer<OptionalCollection<Object>> consumer) {
            consumerMap.put(predicate, consumer);
            return this;
        }

        public Map<Predicate<Object>, Consumer<OptionalCollection<Object>>> build() {
            return consumerMap;
        }
    }


    public static class ExGenerator {
        final Map<Object, List<Object>> map;
        final Function<Object, Object> signFunction;
        final int max;
        final Map<Predicate<Object>, Consumer<OptionalCollection<Object>>> consumerMap;
        long count;

        public ExGenerator(int max, Map<Predicate<Object>, Consumer<OptionalCollection<Object>>> consumerMap, Function<Object, Object> signFunction) {
            this.max = max;
            this.consumerMap = consumerMap;
            this.signFunction = signFunction;
            map = new HashMap<>();
        }


        public void add(Object object) {
            Object sign = signFunction.apply(object);
            List<Object> list = map.computeIfAbsent(sign, k -> new ArrayList<>(max));
            list.add(object);
            if (list.size() == max) {
                foreach(sign, list);
                count += max;
                list.clear();
            }
        }

        private void foreach(Object sign, List<Object> list) {
            consumerMap.entrySet().stream()
                    .filter(entry -> entry.getKey().test(sign))
                    // 这里新建一个 ArrayList 赋值引用是防止并发问题
                    .forEach(handler -> {
                        List<Object> data = new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(list)) {
                            data.addAll(list);
                        }
                        handler.getValue().accept(OptionalCollection.of(data));
                    });
        }

        private void end() {
            map.forEach(this::foreach);
        }
    }

    public static class Generator<T> {
        final List<T> list;
        final int max;
        final Consumer<OptionalCollection<T>> handler;
        long count;

        public Generator(int max, Consumer<OptionalCollection<T>> handler) {
            this.max = max;
            this.handler = handler;
            list = new ArrayList<>(max);
        }


        public void add(T object) {
            list.add(object);
            if (list.size() == max) {
                count += max;
                consume();
                list.clear();
            }
        }

        private void consume() {
            List<T> data = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)) {
                data.addAll(list);
            }
            handler.accept(OptionalCollection.of(data));
        }

        private void end() {
            count += list.size();
            consume();
        }
    }
}
