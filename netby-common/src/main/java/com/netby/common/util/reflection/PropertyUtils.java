package com.netby.common.util.reflection;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * 类属性工具
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class PropertyUtils {

    /**
     * 获取一个对象的字段值
     *
     * @param field  字段
     * @param object 对象
     * @param <T>    target class
     * @return field value
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows(Exception.class)
    public static <T> T getValue(Field field, Object object) {
        boolean accessible = field.isAccessible();
        try {
            if (!accessible) {
                field.setAccessible(true);
            }
            return (T) field.get(object);
        } finally {
            // 设置回去，保证安全
            field.setAccessible(accessible);
        }
    }

    /**
     * 获取一个对象的字段值
     *
     * @param fieldName 字段名
     * @param object    对象
     * @param <T>       target class
     * @return field value
     */
    public static <T> T getValue(String fieldName, Object object) {
        Field field = FieldUtils.getField(object.getClass(), fieldName);
        return getValue(field, object);
    }

    /**
     * 设置属性
     *
     * @param field  字段
     * @param object 对象
     * @param value  新值
     */
    @SneakyThrows(Exception.class)
    public static void setValue(Field field, Object object, Object value) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } finally {
            field.setAccessible(accessible);
        }
    }
}
