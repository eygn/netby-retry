package com.netby.retry.enums;

/**
 * @author: Elvan.bai
 * @date: 2023/7/16 14:00
 */
public enum OpenBizRetryStatusEnum {

    INIT(0),
    SUCCESS(1),
    FAIL(2),

    ;
    private Integer code;

    OpenBizRetryStatusEnum(Integer code) {
        this.code = code;
    }
}
