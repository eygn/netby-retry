package com.netby.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    private static final TypeReference<LinkedHashMap<String, Object>> MAP =
        new TypeReference<LinkedHashMap<String, Object>>() {
        };

    private static final TypeReference<LinkedHashMap<String, String>> MAP_STRING =
        new TypeReference<LinkedHashMap<String, String>>() {
        };

    private static final TypeReference<List<LinkedHashMap<String, Object>>> LIST_MAP =
        new TypeReference<List<LinkedHashMap<String, Object>>>() {
        };

    static {
        OBJECT_MAPPER = JsonMapper.builder()
            //反序列化的时候如果多了其他属性,不抛出异常
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            //如果是空对象的时候,不抛异常
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            //按照首字母排序
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            //属性为null的转换
            .serializationInclusion(Include.NON_NULL)
            //时间序列化问题
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .addModule(new JavaTimeModule())
            .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .build();
    }

    public static String writeValueAsString(Object o) {
        try {
            if (o == null) {
                return "";
            }
            if (o instanceof String || o.getClass().isPrimitive()) {
                return o.toString();
            } else {
                return OBJECT_MAPPER.writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(String json, Type type) {
        return readValue(json, OBJECT_MAPPER.getTypeFactory().constructType(type));
    }

    public static <T> T readValue(String json, TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(String json, JavaType type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readAsList(String json, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        return readValue(json, javaType);
    }

    public static List<LinkedHashMap<String, Object>> readAsListMap(String json) {
        return readValue(json, LIST_MAP);
    }

    public static <T> T convertValue(Object values, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(values, clazz);
    }

    public static Map<String, Object> convertToMap(Object values) {
        return OBJECT_MAPPER.convertValue(values, MAP);
    }

    public static Map<String, String> convertToMapString(Object values) {
        return OBJECT_MAPPER.convertValue(values, MAP_STRING);
    }

    public static <T> T readValue(String values, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(values, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> readValueAsMap(String values) {
        try {
            return OBJECT_MAPPER.readValue(values, MAP);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static JavaType getJavaType(Class<?> parametrized, Class<?>... parameterClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static JavaType getJavaType(Class<?> parametrized, Type subType) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, OBJECT_MAPPER.getTypeFactory().constructType(subType));
    }
}