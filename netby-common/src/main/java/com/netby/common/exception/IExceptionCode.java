package com.netby.common.exception;

/**
 * Title: 错误代码枚举类
 *
 * @author: byg
 */
public interface IExceptionCode {

    /**
     * 错误代码
     *
     * @return String
     */
    Integer getCode();

    /**
     * 错误内容
     *
     * @return String
     */
    String getMessage();
}
