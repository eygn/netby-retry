package com.netby.biz.retry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netby.biz.retry.entity.BizRetryDO;
import com.netby.biz.retry.enums.RetryStatusEnum;

/**
 * 服务重试表(BizRetry)表服务接口
 *
 * @author: byg
 * @since 2023-06-17 17:09:50
 */
public interface BizRetryService extends IService<BizRetryDO> {

    /**
     * 更新重试
     *
     * @param riskDecisionRetryDO
     * @param retryStatusEnum
     */
    void updateRetry(BizRetryDO riskDecisionRetryDO, RetryStatusEnum retryStatusEnum);

}

