package com.netby.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.netby.common.exception.BusinessException;
import com.netby.common.exception.IExceptionCode;
import com.netby.common.util.StringUtil;
import lombok.Data;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author: byg
 */
@Data
@SuppressWarnings("unused")
public class Response<T> implements Serializable {

    private Integer code;
    private String message;
    T data;

    public Response() {
        this.code = 500;
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(IExceptionCode status, T data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isSuccess() {
        return this.code == 200;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "SUCCESS", data);
    }

    public static <T> Response<T> success() {
        return new Response<>(200, "SUCCESS", null);
    }

    public static <T> Response<T> success(String msg, T data) {
        return new Response<>(200, msg, data);
    }

    public static <T> Response<T> failed(IExceptionCode status) {
        return new Response<>(status, null);
    }

    public static <T> Response<T> failed(IExceptionCode e, Object... args) {
        String msg = StringUtil.isBlank(e.getMessage()) ? "" : e.getMessage().contains("%s") ?
                String.format(e.getMessage(), args) : MessageFormat.format(e.getMessage(), args);
        return new Response<>(e.getCode(), msg, null);
    }

    public static <T> Response<T> failed(BusinessException e) {
        return new Response<>(e.getCode(), e.getMessage(), null);
    }

    public static <T> Response<T> failed(BusinessException e, Object... args) {
        String msg = StringUtil.isBlank(e.getMessage()) ? "" : e.getMessage().contains("%s") ?
                String.format(e.getMessage(), args) : MessageFormat.format(e.getMessage(), args);
        return new Response<>(e.getCode(), msg, null);
    }

    public static <T> Response<T> failed(Integer code, String message) {
        return new Response<>(code, message, null);
    }
}
