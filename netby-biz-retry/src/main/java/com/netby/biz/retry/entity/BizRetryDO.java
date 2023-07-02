package com.netby.biz.retry.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务重试表(BizRetry)表实体类
 *
 * @author: byg
 * @since 2023-06-17 17:09:48
 */
@SuppressWarnings("serial")
@TableName("biz_retry")
public class BizRetryDO extends Model<BizRetryDO> {
    //主键
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizParams() {
        return bizParams;
    }

    public void setBizParams(String bizParams) {
        this.bizParams = bizParams;
    }

    public String getRetryType() {
        return retryType;
    }

    public void setRetryType(String retryType) {
        this.retryType = retryType;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getRetryStatus() {
        return retryStatus;
    }

    public void setRetryStatus(Integer retryStatus) {
        this.retryStatus = retryStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

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

