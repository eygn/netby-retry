package com.netby.retry.dto;

import com.netby.common.vo.QueryPage;
import com.netby.retry.enums.OpenBizRetryStatusEnum;
import com.netby.retry.enums.OpenRetryBizTypeEnum;
import lombok.Data;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:58
 */
@Data
public class BizRetryListQuery extends QueryPage {

    /**
     * 重试状态
     * {@link OpenBizRetryStatusEnum}
     */
    private Integer retryStatus;

    /**
     * 重试类型
     * {@link OpenRetryBizTypeEnum}
     */
    private String retryType;

    /**
     * 业务类型
     */
    private String bizType;
}
