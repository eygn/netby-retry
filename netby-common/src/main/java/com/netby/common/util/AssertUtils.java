package com.netby.common.util;

import com.netby.common.exception.BusinessException;
import com.netby.common.exception.IExceptionCode;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Objects;

/**
 * Assert工具类
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class AssertUtils {

    private static void error(Integer code, String message) {
        throw new BusinessException(code, message);
    }

    private static void error(IExceptionCode exceptionCode) {
        throw new BusinessException(exceptionCode);
    }

    private static void error(IExceptionCode exceptionCode, String message) {
        throw new BusinessException(exceptionCode.getCode(), StringUtil.isEmpty(message) ? exceptionCode.getMessage() : message);
    }

    public static void isNull(Object object, Integer code, String message) {
        if (object != null) {
            error(code, message);
        }
    }

    public static void isNull(Object object, IExceptionCode exceptionCode) {
        if (object != null) {
            error(exceptionCode);
        }
    }

    public static void isNull(Object object, IExceptionCode exceptionCode, String message) {
        if (object != null) {
            error(exceptionCode, message);
        }
    }

    public static void notNull(Object object, Integer code, String message) {
        if (object == null) {
            error(code, message);
        }
    }

    public static void notNull(Object object, IExceptionCode exceptionCode) {
        if (object == null) {
            error(exceptionCode);
        }
    }

    public static void notNull(Object object, IExceptionCode exceptionCode, String message) {
        if (object == null) {
            error(exceptionCode, message);
        }
    }

    public static void isTrue(boolean expression, Integer code, String message) {
        if (!expression) {
            error(code, message);
        }
    }

    public static void isTrue(boolean expression, IExceptionCode exceptionCode) {
        if (!expression) {
            error(exceptionCode);
        }
    }
    public static void isTrueArgs(boolean expression, IExceptionCode e,Object... args) {
        if (!expression) {
            String msg = StringUtil.isBlank(e.getMessage()) ? "" : e.getMessage().contains("%s") ?
                    String.format(e.getMessage(), args) : MessageFormat.format(e.getMessage(), args);
            error(e,msg);
        }
    }

    public static void isTrue(boolean expression, IExceptionCode exceptionCode, String message) {
        if (!expression) {
            error(exceptionCode, message);
        }
    }

    public static void isFalse(boolean expression, Integer code, String message) {
        if (expression) {
            error(code, message);
        }
    }

    public static void isFalse(boolean expression, IExceptionCode exceptionCode) {
        if (expression) {
            error(exceptionCode);
        }
    }

    public static void isFalse(boolean expression, IExceptionCode exceptionCode, String message) {
        if (expression) {
            error(exceptionCode, message);
        }
    }

    public static void eq(Object a, Object b, Integer code, String message) {
        if (!Objects.equals(a, b)) {
            error(code, message);
        }
    }

    public static void eq(Object a, Object b, IExceptionCode exceptionCode) {
        if (!Objects.equals(a, b)) {
            error(exceptionCode);
        }
    }

    public static void eq(Object a, Object b, IExceptionCode exceptionCode, String message) {
        if (!Objects.equals(a, b)) {
            error(exceptionCode, message);
        }
    }

    public static void not(Object a, Object b, Integer code, String message) {
        if (Objects.equals(a, b)) {
            error(code, message);
        }
    }

    public static void not(Object a, Object b, IExceptionCode exceptionCode) {
        if (Objects.equals(a, b)) {
            error(exceptionCode);
        }
    }

    public static void not(Object a, Object b, IExceptionCode exceptionCode, String message) {
        if (Objects.equals(a, b)) {
            error(exceptionCode, message);
        }
    }

    public static void empty(String text, Integer code, String message) {
        if (StringUtil.isNotEmpty(text)) {
            error(code, message);
        }
    }

    public static void empty(String text, IExceptionCode exceptionCode) {
        if (StringUtil.isNotEmpty(text)) {
            error(exceptionCode);
        }
    }

    public static void empty(String text, IExceptionCode exceptionCode, String message) {
        if (StringUtil.isNotEmpty(text)) {
            error(exceptionCode, message);
        }
    }

    public static void notEmpty(String text, Integer code, String message) {
        if (StringUtil.isEmpty(text)) {
            error(code, message);
        }
    }

    public static void notEmpty(String text, IExceptionCode exceptionCode) {
        if (StringUtil.isEmpty(text)) {
            error(exceptionCode);
        }
    }

    public static void notEmpty(String text, IExceptionCode exceptionCode, String message) {
        if (StringUtil.isEmpty(text)) {
            error(exceptionCode, message);
        }
    }


    public static void emptyCollection(Collection<?> list, Integer code, String message) {
        if (isNotEmpty(list)) {
            error(code, message);
        }
    }

    public static void emptyCollection(Collection<?> list, IExceptionCode exceptionCode) {
        if (isNotEmpty(list)) {
            error(exceptionCode);
        }
    }

    public static void emptyCollection(Collection<?> list, IExceptionCode exceptionCode, String message) {
        if (isNotEmpty(list)) {
            error(exceptionCode, message);
        }
    }

    public static void notEmptyCollection(Collection<?> list, Integer code, String message) {
        if (isEmpty(list)) {
            error(code, message);
        }
    }

    public static void notEmptyCollection(Collection<?> list, IExceptionCode exceptionCode) {
        if (isEmpty(list)) {
            error(exceptionCode);
        }
    }

    public static void notEmptyCollection(Collection<?> list, IExceptionCode exceptionCode, String message) {
        if (isEmpty(list)) {
            error(exceptionCode, message);
        }
    }

    private static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    private static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
}
