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
public class SystemException extends BaseException {

    @Override
    public boolean isShowStackTrace() {
        return true;
    }

    public SystemException() {
        super();
    }

    public SystemException(Throwable e) {
        super(500, e.getMessage());
    }

    public SystemException(Integer code, String message) {
        super(code, message);
    }

    public SystemException(String message) {
        super(500, message);
    }

    public SystemException(IExceptionCode code) {
        super(code.getCode(), code.getMessage());
    }

    public SystemException(IExceptionCode code, Object... args) {
        super(code.getCode(), StringUtil.isBlank(code.getMessage()) ? "" : code.getMessage().contains("%s") ?
            String.format(code.getMessage(), args) : MessageFormat.format(code.getMessage(), args));
    }

    public SystemException(Response<?> cause) {
        super(cause.getCode(), cause.getMessage());
    }
}
