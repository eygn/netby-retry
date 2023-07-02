package com.netby.common.exception;

import com.netby.common.util.StringUtil;
import com.netby.common.vo.Response;

import java.text.MessageFormat;

/**
 * Title: 商业逻辑错误
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class BusinessException extends BaseException {

    public BusinessException() {
        super();
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(String message) {
        super(201, message);
    }

    public BusinessException(IExceptionCode code) {
        super(code.getCode(), code.getMessage());
    }

    public BusinessException(IExceptionCode code, Object... args) {
        super(code.getCode(), StringUtil.isBlank(code.getMessage()) ? "" : code.getMessage().contains("%s") ?
                String.format(code.getMessage(), args) : MessageFormat.format(code.getMessage(), args));
    }

    public BusinessException(Response<?> cause) {
        super(cause.getCode(), cause.getMessage());
    }
}
