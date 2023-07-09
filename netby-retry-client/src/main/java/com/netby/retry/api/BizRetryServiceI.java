package com.netby.retry.api;

import com.alibaba.cola.dto.Response;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryUpdateCmd;

public interface BizRetryServiceI {

    /**
     * 新增重试
     *
     * @param bizRetryAddCmd
     * @return
     */
    Response addBizRetry(BizRetryAddCmd bizRetryAddCmd);

    /**
     * 更新重试
     *
     * @param bizRetryUpdateCmd
     */
    void updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd);

}
