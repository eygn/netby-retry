package com.netby.common.vo.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件
 *
 * @author byg
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Expression {

    /**
     * 对应字段，自动转换驼峰结构
     *
     * @return 对应字段
     */
    String column() default "";

    /**
     * 操作
     *
     * @return 操作类型
     */
    SearchOperator operator() default SearchOperator.EQUAL;

    /**
     * 连接符
     *
     * @return 连接符
     */
    SearchJoiner joiner() default SearchJoiner.AND;

    /**
     * 组
     *
     * @return 组ID
     */
    int group() default 0;

    /**
     * 组连接符
     *
     * @return 连接符
     */
    SearchJoiner groupJoiner() default SearchJoiner.AND;

    /**
     * 当字符串或者集合为空时是否生效
     *
     * @return 是否生效
     */
    boolean activeIfEmpty() default false;
}
