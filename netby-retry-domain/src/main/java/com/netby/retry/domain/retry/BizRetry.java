package com.netby.retry.domain.retry;


import com.alibaba.cola.domain.Entity;
import com.netby.common.vo.BaseVO;
import lombok.Data;

import java.util.Date;

/**
 * 服务重试
 *
 * @author: Elvan.bai
 * @since 2023-06-17 17:09:48
 */
@SuppressWarnings("serial")
@Data
@Entity
public class BizRetry extends BaseVO {

    private Integer id;
    //业务编号
    private String bizNo;
    //业务类型
    private String bizType;
    //业务参数
    private String bizParams;
    //重试类型
    private String retryType;
    //重试次数
    private Integer retryCount;
    //重试状态(0:待重试; 1:重试成功; 2:重试失败)
    private Integer retryStatus;
    //备注
    private String comment;
    //创建时间
    private Date created;
    //修改时间
    private Date modified;
    //数据是否有效
    private Integer flag;

}

