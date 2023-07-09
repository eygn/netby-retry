package com.netby.retry.dto.event;

/**
 * BizRetryCreatedEvent
 *
 * @author baiyuangang
 * @date 2023/7/9 10:52
 */
public class BizRetryCreatedEvent {

    private String bizRetryId;

    public String getBizRetryId() {
        return bizRetryId;
    }

    public void setBizRetryId(String bizRetryId) {
        this.bizRetryId = bizRetryId;
    }

}
