package com.netby.retry.dto;

import com.alibaba.cola.dto.Query;
import lombok.Data;

@Data
public class BizRetryListQuery extends Query {
    private String name;
}
