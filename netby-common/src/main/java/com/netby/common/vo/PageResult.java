package com.netby.common.vo;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: byg
 */
@Data
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private Long totalCount;
    private Long currentPage;
    private Long pages;
    private Long pageSize;
    private List<T> records;

    public void buildPageResult(Long totalCount, Long currentPage, Long pages, Long pageSize, List<T> records) {
        this.setCurrentPage(currentPage);
        this.setTotalCount(totalCount);
        this.setPages(pages);
        this.setPageSize(pageSize);
        this.setRecords(records);
    }

    public static <S, T> PageResult<T> of(PageResult<S> page, Class<T> clazz) {
        return of(page, clazz, null);
    }

    public static <S, T> PageResult<T> of(PageResult<S> page, Class<T> clazz, BiConsumer<S, T> consumer) {
        List<T> pageList = BaseVO.copyListTo(page.getRecords(), clazz, consumer);
        PageResult<T> result = new PageResult<>();
        result.buildPageResult(page.totalCount, page.currentPage, page.getPages(), page.pageSize, pageList);
        return result;
    }
}
