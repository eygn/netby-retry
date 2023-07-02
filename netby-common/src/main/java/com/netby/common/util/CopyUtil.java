package com.netby.common.util;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.googlecode.jmapper.api.MappedClass;
import com.googlecode.jmapper.util.ClassesManager;
import com.googlecode.jmapper.util.GeneralUtility;
import com.netby.common.exception.SystemException;
import com.netby.common.vo.PairVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static com.googlecode.jmapper.api.JMapperAPI.*;

/**
 * 类拷贝
 *
 * @author: byg
 */
@Slf4j
public class CopyUtil {

    private static final Map<PairVO<String, String>, JMapper<?, ?>> MAPPER = Maps.newConcurrentMap();
    private static final Set<PairVO<String, String>> ERROR_SET = Sets.newConcurrentHashSet();

    private static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ignore) {
            return false;
        }
        return true;
    }

    private static String getExistMethod(Class<?> clazz, String methodName1, String methodName2, Class<?>... paramTypes) {
        if (!hasMethod(clazz, methodName1, paramTypes)) {
            if (hasMethod(clazz, methodName2, paramTypes)) {
                return methodName2;
            } else {
                return null;
            }
        }
        return methodName1;
    }

    private static MappedClass createMappedClass(Map<String, MappedClass> mappedClassList, Class<?> sClass, Class<?> dClass) {
        if (mappedClassList.containsKey(dClass.getName())) {
            return mappedClassList.get(dClass.getName());
        }
        val mapped = mappedClass(dClass);
        mappedClassList.put(dClass.getName(), mapped);
        val sourceSet = ClassesManager.getListOfFields(sClass).stream().filter(f ->
            !Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !f.isSynthetic()
        ).collect(Collectors.toMap(Field::getName, Field::getType, (a, b) -> a));

        int count = 0;
        for (Field field : ClassesManager.getListOfFields(dClass)) {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || field.isSynthetic()
                || Modifier.isTransient(field.getModifiers())
            ) {
                continue;
            }
            if (sourceSet.containsKey(field.getName())) {
                if (field.getType().equals(sourceSet.get(field.getName()))) {
                    if (GeneralUtility.isBoolean(field.getType()) && field.getName().startsWith("is")) {
                        String getSourceMethod = getExistMethod(sClass, GeneralUtility.mGet(field.getName()), field.getName());
                        String getDestMethod = getExistMethod(dClass, GeneralUtility.mGet(field.getName()), field.getName());
                        if (getSourceMethod == null || getDestMethod == null) {
                            Logging.warn(log,
                                () -> log.warn("{} to {} warn,field:{} do not getMethod!", sClass.getSimpleName(),
                                    dClass.getSimpleName(),
                                    field.getName()));
                            continue;
                        }
                        String newSet = GeneralUtility.mSet(StringUtil.findStr(field.getName(), "is", null));
                        String setSourceMethod = getExistMethod(sClass, GeneralUtility.mSet(field.getName()), newSet, field.getType());
                        String setDestMethod = getExistMethod(dClass, GeneralUtility.mSet(field.getName()), newSet, field.getType());
                        if (setSourceMethod == null || setDestMethod == null) {
                            Logging.warn(log,
                                () -> log.warn("{} to {} warn,field:{} do not setMethod!", sClass.getSimpleName(),
                                    dClass.getSimpleName(),
                                    field.getName()));
                            continue;
                        }
                        mapped.add(attribute(field.getName(), getSourceMethod, setSourceMethod).value(
                            targetAttribute(field.getName(), getDestMethod, setDestMethod)));
                    } else {
                        if (!ClassUtil.isPrimitive(field.getType())) {
                            createMappedClass(mappedClassList, field.getType(), field.getType());
                        }
                        mapped.add(attribute(field.getName()).value(field.getName()));
                    }
                    count++;
                } else {
                    Logging.warn(log,
                        () -> log.warn("{} to {} warn,field:{} type do not match:[{} to {}]!", sClass.getSimpleName(), dClass.getSimpleName(),
                            field.getName(), sourceSet.get(field.getName()).getSimpleName(), field.getType().getSimpleName()));
                }
            }
        }
        if (count > 0) {
            mappedClassList.put(dClass.getName(), mapped);
            return mapped;
        } else {
            mappedClassList.remove(dClass.getName());
            return null;
        }
    }

    /**
     * 拷贝
     *
     * @param source 值
     * @param dClass 目标
     * @param <D>    目标
     * @param <S>    原
     * @return 目标值
     */
    @SuppressWarnings("unchecked")
    public static <D, S> D copy(S source, Class<D> dClass) {

        val key = new PairVO<>(source.getClass().getName(), dClass.getName());

        JMapper<D, S> mapper;

        // TODO 错误兼容模式，在1.2.0版本以前保持这种兼容；
        if (ERROR_SET.contains(key)) {
            return newInstance(dClass);
        }

        mapper = (JMapper<D, S>) MAPPER.computeIfAbsent(key, kk -> {
            Map<String, MappedClass> mappedClassList = Maps.newHashMap();
            val mapped = createMappedClass(mappedClassList, source.getClass(), dClass);

            if (mapped != null) {
                val api = new JMapperAPI();
                mappedClassList.forEach((k, v) -> api.add(v));
                return new JMapper<>(dClass, source.getClass(), api);
            } else {
                ERROR_SET.add(key);
                Logging.error(log,
                    () -> log.error("{} to {} error,field is empty!", source.getClass().getSimpleName(), dClass.getSimpleName()));
                return null;
            }
        });
        if (mapper != null) {
            return mapper.getDestination(source);
        } else {
            return newInstance(dClass);
        }
    }

    private static <D> D newInstance(Class<D> dClass) {
        try {
            return dClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param src      数据源
     * @param clz      目标类
     * @param consumer 拷贝后处理
     * @return 结果
     */
    public static <A, B> B copy(A src, Class<B> clz, BiConsumer<A, B> consumer) {
        if (src == null) {
            return null;
        }
        B data = copy(src, clz);
        if (consumer != null && data != null) {
            consumer.accept(src, data);
        }
        return data;
    }

    private static <A, B, C extends Collection<B>> C copy(Collection<A> sources, Class<B> clz, Supplier<C> supplier, BiConsumer<A, B> consumer) {
        if (sources == null) {
            return null;
        }
        C newList = supplier.get();
        for (A a : sources) {
            B clone = CopyUtil.copy(a, clz);
            if (consumer != null && clone != null) {
                consumer.accept(a, clone);
            }
            newList.add(clone);
        }
        return newList;
    }


    /**
     * 拷贝一个列表
     *
     * @param sources 数据源
     * @param clz     目标类
     * @return 结果
     */
    public static <A, B> List<B> copy2List(Collection<A> sources, Class<B> clz) {
        return copy(sources, clz, ArrayList::new, null);
    }

    public static <A, B> List<B> copy2List(Collection<A> sources, Class<B> clz, BiConsumer<A, B> consumer) {
        return copy(sources, clz, ArrayList::new, consumer);
    }

    /**
     * 拷贝一个Set
     *
     * @param sources 数据源
     * @param clz     目标类
     * @return 结果
     */
    public static <A, B> Set<B> copy2Set(Collection<A> sources, Class<B> clz) {
        return copy(sources, clz, HashSet::new, null);
    }

    public static <A, B> Set<B> copy2Set(Collection<A> sources, Class<B> clz, BiConsumer<A, B> consumer) {
        return copy(sources, clz, HashSet::new, consumer);
    }
}
