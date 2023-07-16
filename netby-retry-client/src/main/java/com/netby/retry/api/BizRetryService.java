package com.netby.retry.api;

import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:49
 */
public interface BizRetryService {

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
     * @return
     */
    Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd);

    /**
     * 根据业务类型查询
     *
     * @param bizRetryListQuery
     * @return
     */
    PageResult<BizRetryDTO> listByBizType(BizRetryListQuery bizRetryListQuery);

}