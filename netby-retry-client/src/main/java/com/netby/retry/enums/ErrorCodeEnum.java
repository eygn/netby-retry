package com.netby.retry.enums;

/**
 * @author Elvan.bai
 * @date 2023/7/16 14:03
 */
public enum ErrorCodeEnum {
    B_CUSTOMER_companyNameConflict("B_CUSTOMER_companyNameConflict", "客户公司名冲突");

    private final String errCode;
    private final String errDesc;

    ErrorCodeEnum(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }
}
