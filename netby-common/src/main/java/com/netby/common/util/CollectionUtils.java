package com.netby.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author: byg
 */
public class CollectionUtils {

    public static boolean isEmpty(Object[] coll) {
        return coll == null || coll.length == 0;
    }

    public static boolean isNotEmpty(Object[] coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> T getFirst(Collection<T> coll) {
        if (isNotEmpty(coll)) {
            return coll.stream().findFirst().orElse(null);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFirst(Object[] coll) {
        if (isNotEmpty(coll)) {
            return (T) coll[0];
        }
        return null;
    }

    public static <T> T getFirst(Map<?, T> map) {
        if (isNotEmpty(map)) {
            return map.values().stream().findFirst().orElse(null);
        }
        return null;
    }
}
