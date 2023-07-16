package com.netby.retry.dto.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:52
 */
@Data
public class BizRetryDTO implements Serializable {

    private static final long serialVersionUID = 996870170144647648L;

    private Integer id;
    /**
     * 业务编号
     */
    private String bizNo;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 业务参数
     */
    private String bizParams;
    /**
     * 重试类型
     */
    private String retryType;
    /**
     * 重试次数
     */
    private Integer retryCount;
    /**
     * 重试状态(0:待重试; 1:重试成功; 2:重试失败)
     */
    private Integer retryStatus;
    /**
     * 备注
     */
    private String comment;
}
