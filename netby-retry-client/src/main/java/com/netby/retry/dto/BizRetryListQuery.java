package com.netby.retry.dto;

import com.netby.common.vo.QueryPage;
import lombok.Data;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:58
 */
@Data
public class BizRetryListQuery extends QueryPage {

    /**
     * 业务类型
     */
    private String bizType;
}
