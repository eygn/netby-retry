package com.netby.common.vo.search;

/**
 * 搜索操作
 *
 * @author: byg
 */
public enum SearchOperator {
    /**
     * 不参与自动生成
     */
    NONE,
    /**
     * 判断是否为NULL，本字段类型必须为Boolean
     */
    NULL,
    EQUAL,
    NOT_EQUAL,
    LESS_THEN,
    LESS_THEN_EQUAL,
    GREATER_THEN,
    GREATER_THEN_EQUAL,
    LIKE,
    LIKE_LEFT,
    LIKE_RIGHT,
    NOT_LIKE,
    /**
     * 该操作，本字段必须为Collection类型，有2个值，不然会报错
     */
    BETWEEN,
    /**
     * 该操作，本字段必须为Collection类型，有2个值，不然会报错
     */
    NOT_BETWEEN,
    /**
     * 该操作，本字段必须为Collection类型，最少有有1个及以上的值，不然会报错
     */
    IN,
    /**
     * 该操作，本字段必须为Collection类型，最少有有1个及以上的值，不然会报错
     */
    NOT_IN
}
