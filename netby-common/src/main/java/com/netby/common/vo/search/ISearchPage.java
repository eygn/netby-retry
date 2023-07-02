/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netby.common.vo.search;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 分页 Page 对象接口
 *
 * @author: byg
 * @since 2018-06-09
 */
@SuppressWarnings("unused")
public interface ISearchPage<T> extends Serializable {

    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    List<OrderItem> orders();

    /**
     * 自动优化 COUNT SQL【 默认：true 】
     *
     * @return true 是 / false 否
     */
    default boolean optimizeCountSql() {
        return true;
    }

    /**
     * 两个参数都为 true 才会进行sql处理
     *
     * @return true 是 / false 否
     * @since 3.4.4 @2021-09-13
     */
    default boolean optimizeJoinOfCountSql() {
        return true;
    }

    /**
     * 进行 count 查询 【 默认: true 】
     *
     * @return true 是 / false 否
     */
    default boolean searchCount() {
        return true;
    }

    /**
     * 计算当前分页偏移量
     *
     * @return 偏移量
     */
    default long offset() {
        long current = getCurrent();
        if (current <= 1L) {
            return 0L;
        }
        return Math.max((current - 1) * getSize(), 0L);
    }

    /**
     * 最大每页分页数限制,优先级高于分页插件内的 maxLimit
     *
     * @return 最大分页数
     * @since 3.4.0 @2020-07-17
     */
    default Long maxLimit() {
        return null;
    }

    /**
     * 当前分页总页数
     *
     * @return 分页总数
     */
    default long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 内部什么也不干
     * <p>只是为了 json 反序列化时不报错</p>
     *
     * @param pages pages
     * @return this
     */
    default ISearchPage<T> setPages(long pages) {
        // to do nothing
        return this;
    }

    /**
     * 分页记录列表
     *
     * @return 分页对象记录列表
     */
    List<T> getRecords();

    /**
     * 设置分页记录列表
     *
     * @param records 记录列表
     * @return this
     */
    ISearchPage<T> setRecords(List<T> records);

    /**
     * 当前满足条件总行数
     *
     * @return 总条数
     */
    long getTotal();

    /**
     * 设置当前满足条件总行数
     *
     * @param total 总行数
     * @return this
     */
    ISearchPage<T> setTotal(long total);

    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数
     */
    long getSize();

    /**
     * 设置每页显示条数
     *
     * @param size 每页显示条数
     * @return this
     */
    ISearchPage<T> setSize(long size);

    /**
     * 当前页
     *
     * @return 当前页
     */
    long getCurrent();

    /**
     * 设置当前页
     *
     * @param current 当前页
     * @return this
     */
    ISearchPage<T> setCurrent(long current);

    /**
     * IPage 的泛型转换
     *
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换泛型后的 IPage
     */
    @SuppressWarnings("unchecked")
    default <R> ISearchPage<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((ISearchPage<R>) this).setRecords(collect);
    }

    /**
     * 老分页插件不支持
     * <p>
     * MappedStatement 的 id
     *
     * @return id
     * @since 3.4.0 @2020-06-19
     */
    default String countId() {
        return null;
    }
}
