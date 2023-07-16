package com.netby.retry.mybatis.bizretry;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务重试表(BizRetry)表实体类
 *
 * @author: Elvan.bai
 * @since 2023-06-17 17:09:48
 */
@Data
@TableName("biz_retry")
public class BizRetryDO extends Model<BizRetryDO> {

    /**
     * 主键
     */
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
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 修改时间
     */
    private Date modified;
    /**
     * 数据是否有效
     */
    private Integer flag;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

