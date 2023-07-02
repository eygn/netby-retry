package com.netby.common.vo;

import java.io.Serializable;
import java.util.Collections;

import com.netby.common.util.StringUtil;
import com.netby.common.vo.search.AbstractSearchParam;
import com.netby.common.vo.search.Expression;
import com.netby.common.vo.search.OrderItem;
import com.netby.common.vo.search.SearchOperator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * @author: byg
 */
@Getter
@Accessors(chain = true)
@SuppressWarnings("unused")
public class QueryPage extends AbstractSearchParam implements Serializable {

    private static final long serialVersionUID = -2392301061362669120L;

    /**
     * 页码
     */
    @Expression(operator = SearchOperator.NONE)
    private Integer pageNo = 1;
    /**
     * 每页条数
     */
    @Expression(operator = SearchOperator.NONE)
    private Integer pageSize = 10;
    /**
     * 排序字段
     */
    @Expression(operator = SearchOperator.NONE)
    private String orderByColumn;
    /**
     * 是否正序
     */
    @Expression(operator = SearchOperator.NONE)
    @Setter
    private Boolean isAsc = false;

    public Integer getSkip() {
        return pageSize * (pageNo - 1);
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        if (pageNo != null && pageSize != null) {
            setPages(new Integer[]{pageNo, pageSize});
        }
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if (pageNo != null && pageSize != null) {
            setPages(new Integer[]{pageNo, pageSize});
        }
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
        if (StringUtil.isNotEmpty(orderByColumn)) {
            setOrderList(Collections.singletonList(new OrderItem(orderByColumn, isAsc)));
        }
    }
}
