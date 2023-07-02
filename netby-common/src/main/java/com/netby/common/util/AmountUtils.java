package com.netby.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额帮助类
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class AmountUtils {

    private static final BigDecimal BASE = BigDecimal.valueOf(10000);
    private static final int DECIMAL_LIMIT = 4;


    /**
     * 将金额 * 10000，即小数点右移四位，如果金额小数位数大于 4位，则抛出异常
     *
     * @param amount 金额
     * @return 金额
     */
    public static BigDecimal expansion(BigDecimal amount) {
        int scale = amount.scale();
        if (scale > DECIMAL_LIMIT) {
            throw new IllegalArgumentException("小数位数过大");
        }
        return amount.multiply(BASE);
    }

    public static BigDecimal expansion(double amount) {
        return expansion(BigDecimal.valueOf(amount));
    }

    public static BigDecimal expansion(String amount) {
        return expansion(new BigDecimal(amount));
    }

    /**
     * 将金额 / 10000，即将小数点左移四位
     *
     * @param bigDecimal 金额
     * @return 金额
     */
    public static BigDecimal recover(BigDecimal bigDecimal) {
        BigDecimal divide = bigDecimal.divide(BASE, 4, RoundingMode.HALF_UP);
        double result = bigDecimal.longValue() / 10000d;
        if (divide.compareTo(BigDecimal.valueOf(result)) != 0) {
            throw new IllegalArgumentException("金额复原错误");
        }
        return divide;
    }

    public static BigDecimal recover(long amount) {
        return recover(BigDecimal.valueOf(amount));
    }

    public static BigDecimal recover(String amount) {
        return recover(new BigDecimal(amount));
    }

    /**
     * 金额相加 a+b
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return null2Zero(a).add(null2Zero(b));
    }

    /**
     * 金额相减 a-b
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return null2Zero(a).subtract(null2Zero(b));
    }

    public static BigDecimal null2Zero(BigDecimal a) {
        return a == null ? BigDecimal.ZERO : a;
    }

    public static BigDecimal null2Zero(String a) {
        return StringUtil.isBlank(a) ? BigDecimal.ZERO : new BigDecimal(a);
    }

    /**
     * 判断两个金额是否相等
     */
    public static boolean compareEquals(BigDecimal a, BigDecimal b) {
        return null2Zero(a).compareTo(null2Zero(b)) == 0;
    }
    public static boolean compareEqualsAbs(BigDecimal a, BigDecimal b) {
        return null2Zero(a).abs().compareTo(null2Zero(b).abs()) == 0;
    }

    /**
     * 判断两个金额是否相等
     */
    public static boolean compareEquals(String a, String b) {
        return null2Zero(a).compareTo(null2Zero(b)) == 0;
    }

    /**
     * 判断两个金额的绝对值是否相等
     */
    public static boolean compareEqualsAbs(String a, String b) {
        return null2Zero(a).abs().compareTo(null2Zero(b).abs()) == 0;
    }
}
