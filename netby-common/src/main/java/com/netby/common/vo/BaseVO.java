package com.netby.common.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import com.netby.common.util.CopyUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基础VO
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class BaseVO implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String toMultiLineString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 把自身属性拷贝到其他变量中
     *
     * @param clz 目标类
     * @return 结果
     */
    public <T> T copyTo(Class<T> clz) {
        return CopyUtil.copy(this, clz);
    }


    /**
     * 把自身属性拷贝到其他变量中
     *
     * @param clz 目标类
     * @return 结果
     */
    public static <A, B> B copyTo(A src, Class<B> clz) {
        return copyTo(src, clz, null);
    }

    /**
     * 把属性拷贝到其他变量中
     *
     * @param src 数据源
     * @param clz 目标类
     * @return 结果
     */
    public static <A, B> B copyTo(A src, Class<B> clz, BiConsumer<A, B> consumer) {
        return CopyUtil.copy(src, clz, consumer);
    }

    /**
     * 拷贝一个列表
     *
     * @param sources 数据源
     * @param clz     目标类
     * @return 结果
     */
    public static <A, B> List<B> copyListTo(List<A> sources, Class<B> clz) {
        return copyListTo(sources, clz, null);
    }

    public static <A, B> List<B> copyListTo(List<A> sources, Class<B> clz, BiConsumer<A, B> consumer) {
        return CopyUtil.copy2List(sources, clz, consumer);
    }

    /**
     * 拷贝一个Set
     *
     * @param sources 数据源
     * @param clz     目标类
     * @return 结果
     */
    public static <A, B> Set<B> copySetTo(Set<A> sources, Class<B> clz) {
        return copySetTo(sources, clz, null);
    }

    public static <A, B> Set<B> copySetTo(Set<A> sources, Class<B> clz, BiConsumer<A, B> consumer) {
        return CopyUtil.copy2Set(sources, clz, consumer);
    }
}