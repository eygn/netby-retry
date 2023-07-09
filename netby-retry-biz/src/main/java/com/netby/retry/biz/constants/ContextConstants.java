package com.netby.retry.biz.constants;

/**
 * @author: byg
 */
public interface ContextConstants {

    /**
     * 请求TAG
     */
    String ROUTE_TAG = "ROUTE-TAG";

    /**
     * mse的tag
     */
    String MSE_ROUTE_TAG = "x-mse-tag";

    /**
     * msg请求TAG
     */
    String MSG_ROUTE_TAG = "__route_tag__";
    /**
     * 请求ID
     */
    String ROUTE_ID = "ROUTE-ID";
    /**
     * 请求序列
     */
    String ROUTE_SPAN_ID = "RS-ID";

    /**
     * 页面反馈Exception类
     */
    String HEADER_ERROR = "__ERROR_EX__";
}
