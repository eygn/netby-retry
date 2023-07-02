package com.netby.common.exception;


import com.netby.common.util.StringUtil;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class BaseException extends RuntimeException {

    private boolean showStackTrace = false;
    private Integer code;

    private String causeString;

    public BaseException() {
        super();
    }

    public BaseException(Integer code, String cause) {
        super(cause);
        this.causeString = cause;
        this.code = code;
    }

    public boolean isShowStackTrace() {
        return showStackTrace;
    }

    public void setShowStackTrace(boolean showStackTrace) {
        this.showStackTrace = showStackTrace;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        String message = "";
        if (StringUtil.isNotEmpty(this.causeString)) {
            message = this.causeString;
        }
        return "{code:\"" + code + "\",cause:\"" + message + "\"}";
    }

    public String getCauseString() {
        return this.causeString;
    }

    /**
     * 覆盖该告警，去除同步，无需stack信息，通过改配置可以提高大约100倍以上性能
     *
     * @return Throwable
     */
    @Override
    public Throwable fillInStackTrace() {
        if (isShowStackTrace()) {
            return super.fillInStackTrace();
        }
        return this;
    }
}
