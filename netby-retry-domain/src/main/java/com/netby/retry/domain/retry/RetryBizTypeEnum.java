package com.netby.retry.domain.retry;

/**
 * 重试业务类型枚举
 *
 * @author: byg
 * @date 2023/3/27 16:04
 */
public enum RetryBizTypeEnum {

    DECISION("decision", "决策重试"),
    NOTIFY("notify", "通知重试"),
    ;

    /**
     * 重试编码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    RetryBizTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
