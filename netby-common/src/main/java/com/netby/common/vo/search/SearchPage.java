package com.netby.common.vo.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Setter;


/**
 * 简单分页模型
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class SearchPage<T> implements ISearchPage<T> {

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

    /**
     * 排序字段信息
     */
    @Setter
    protected List<OrderItem> orders = new ArrayList<>();

    /**
     * 自动优化 COUNT SQL
     */
    protected boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    protected boolean searchCount = true;
    /**
     * countId
     */
    @Setter
    protected String countId;
    /**
     * countId
     */
    @Setter
    protected Long maxLimit;

    public SearchPage() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public SearchPage(long current, long size) {
        this(current, size, 0);
    }

    public SearchPage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public SearchPage(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public SearchPage(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public SearchPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public SearchPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public SearchPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public SearchPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public String countId() {
        return this.countId;
    }

    @Override
    public Long maxLimit() {
        return this.maxLimit;
    }

    /**
     * 添加新的排序条件
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public SearchPage<T> addOrder(OrderItem... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加新的排序条件
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public SearchPage<T> addOrder(List<OrderItem> items) {
        orders.addAll(items);
        return this;
    }

    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    @Override
    public boolean searchCount() {
        if (total < 0) {
            return false;
        }
        return searchCount;
    }

    @Override
    public long getPages() {
        return ISearchPage.super.getPages();
    }

    public static <T> SearchPage<T> of(long current, long size) {
        return of(current, size, 0);
    }

    public static <T> SearchPage<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    public static <T> SearchPage<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0, searchCount);
    }

    public static <T> SearchPage<T> of(long current, long size, long total, boolean searchCount) {
        return new SearchPage<>(current, size, total, searchCount);
    }

}
