package com.netby.retry.dto;

import com.alibaba.cola.dto.Query;
import lombok.Data;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:58
 */
@Data
public class BizRetryListQuery extends Query {

    /**
     * 业务类型
     */
    private String bizType;
}
