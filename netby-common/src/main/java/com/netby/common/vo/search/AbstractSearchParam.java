package com.netby.common.vo.search;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.netby.common.util.StringUtil;
import com.netby.common.vo.BaseVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: byg
 */
@Slf4j
@Getter
@SuppressWarnings({"unchecked", "unused"})
public abstract class AbstractSearchParam extends BaseVO {

    private static final Integer PAGE_ARRAY_LIMIT = 2;

    private final List<OrderItem> orderList = Lists.newArrayList();
    private final List<String> columnList = Lists.newArrayList();
    private final Integer[] pages = new Integer[]{1, 10};
    @SuppressWarnings("all")
    private String _orderField_;
    @SuppressWarnings("all")
    private String _orderFieldType_;
    @SuppressWarnings("all")
    private Integer _page_;
    @SuppressWarnings("all")
    private Integer _pageSize_;

    private static final Set<String> ORDER_DESC_TYPE = ImmutableSet.of("descend", "desc", "DESC");

    @SuppressWarnings("all")
    public void set_orderField_(String _orderField_) {
        this._orderField_ = _orderField_;

        if (StringUtil.isNotEmpty(_orderFieldType_)) {
            orderList.add(new OrderItem(_orderField_, !ORDER_DESC_TYPE.contains(_orderFieldType_)));
        }
    }

    @SuppressWarnings("all")
    public void set_orderFieldType_(String _orderFieldType_) {
        this._orderFieldType_ = _orderFieldType_;

        if (StringUtil.isNotEmpty(_orderField_)) {
            orderList.add(new OrderItem(_orderField_, !ORDER_DESC_TYPE.contains(_orderFieldType_)));
        }
    }

    @SuppressWarnings("all")
    public void set_page_(Integer _page_) {
        this._page_ = _page_;
        if (_pageSize_ != null && _pageSize_ > 0) {
            paging(_page_, _pageSize_);
        }
    }

    @SuppressWarnings("all")
    public void set_pageSize_(Integer _pageSize_) {
        this._pageSize_ = _pageSize_;
        if (_page_ != null && _page_ > 0) {
            paging(_page_, _pageSize_);
        }
    }

    public <T extends AbstractSearchParam> T addOrder(boolean isAsc, String column) {
        orderList.add(new OrderItem(column, isAsc));
        return (T) this;
    }

    /**
     * 反序列化时用到
     *
     * @param orderList 初始化列表
     */
    public void setOrderList(List<OrderItem> orderList) {
        this.orderList.addAll(orderList);
    }

    public <T extends AbstractSearchParam> T select(String... columns) {
        this.columnList.addAll(Arrays.asList(columns));
        return (T) this;
    }

    /**
     * 反序列化时用到
     *
     * @param columnList 初始化字段列表
     */
    public void setColumnList(List<String> columnList) {
        this.columnList.addAll(columnList);
    }

    @SuppressWarnings({"unchecked","UnusedReturnValue"})
    public <T extends AbstractSearchParam> T paging(int pageNum, int size) {
        pages[0] = pageNum;
        pages[1] = size;
        return (T) this;
    }

    public Integer[] getPages() {
        return pages;
    }

    public void setPages(Integer[] pages) {
        if (pages == null || pages.length < PAGE_ARRAY_LIMIT) {
            return;
        }
        this.pages[0] = pages[0];
        this.pages[1] = pages[1];
    }

    /**
     * 设置为空的字符串为null
     */
    public <T extends AbstractSearchParam> T clearEmptyValue() {
        try {
            Class<? extends AbstractSearchParam> clazz = this.getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (f.getType().isAssignableFrom(String.class)) {
                    f.setAccessible(true);
                    Object value = f.get(this);
                    if (StringUtil.isEmpty((String) value)) {
                        f.set(this, null);
                    }
                }
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
        return (T) this;
    }

}
