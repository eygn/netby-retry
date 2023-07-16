package com.netby.retry.domain.retry.gateway;

import com.netby.retry.domain.retry.BizRetry;

/**
 * @author Elvan.baiang
 * @date 2023/7/9 11:27
 */
public interface BizRetryGateway {

    BizRetry getBizRetry(String retryId);

    boolean update(BizRetry bizRetry);

    boolean add(BizRetry bizRetry);
}
