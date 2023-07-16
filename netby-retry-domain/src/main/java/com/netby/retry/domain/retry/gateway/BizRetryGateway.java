package com.netby.retry.domain.retry.gateway;

import com.netby.common.vo.PageResult;
import com.netby.common.vo.QueryPage;
import com.netby.retry.domain.retry.BizRetry;

/**
 * @author Elvan.baiang
 * @date 2023/7/9 11:27
 */
public interface BizRetryGateway {

    /**
     * 获取单个bizRetry
     *
     * @param retryId
     * @return
     */
    BizRetry getBizRetry(String retryId);

    /**
     * 根据业务类型查询
     *
     * @param bizType
     * @return
     */
    PageResult<BizRetry> listByBizType(String bizType, QueryPage queryPage);

    /**
     * 更新重试
     *
     * @param bizRetry
     * @return
     */
    boolean update(BizRetry bizRetry);

    /**
     * 新增重试
     *
     * @param bizRetry
     * @return
     */
    BizRetry add(BizRetry bizRetry);
}
