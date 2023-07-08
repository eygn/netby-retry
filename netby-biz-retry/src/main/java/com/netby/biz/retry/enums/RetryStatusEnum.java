package com.netby.biz.retry.enums;

import lombok.Getter;

/**
 * 重试状态枚举
 *
 * @author baiyg
 * @date 2023/7/6 13:12
 */
@Getter
public enum RetryStatusEnum {

    INIT(0),
    SUCCESS(1),
    FAIL(2),

    ;
    private Integer code;

    RetryStatusEnum(Integer code) {
        this.code = code;
    }
}
