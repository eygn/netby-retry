package com.netby.common.util;

import java.util.Random;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class PasswordUtil {

    private static final Random RANDOM = new Random();

    private static final int MIN_PASSWORD_LENGTH = 6;

    private static final int STAGE_POINT = 20;

    /**
     * 得到密码安全率
     *
     * @param password 密码
     * @return 安全率
     */
    public static double getPasswdSecurityRate(String password) {
        return getPasswdSecurityRate(password, null);
    }

    /**
     * 得到密码安全率
     *
     * @param password 密码
     * @param loginId  登录名
     * @return 安全率
     */
    public static double getPasswdSecurityRate(String password, String loginId) {
        // total points
        double points = 0;
        // counter (points)
        double j;
        int jj = 0;
        // numbers
        String n = "0123456789";
        // alpha letters
        String a = "abcdefghijklmnopqrstuvwxyz";
        // upper case count
        int u = 0;
        // lower case count
        int l = 0;
        // alpha numeric
        String an = a + n + a.toUpperCase();

        if (loginId != null) {
            String passwordUpper = password.toUpperCase();
            if (password.contains(loginId)
                || passwordUpper.contains(loginId.toUpperCase())) {
                return 0;
            }
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return 0;
        }
        points = points + ((password.length()) * 2.5);
        if (points > STAGE_POINT) {
            points = STAGE_POINT;
        }

        j = 0;
        for (int i = 0; i < password.length(); i++) {
            if (n.contains(password.substring(i, i + 1))) {
                j = j + 6.67;
                jj++;
            }
        }
        if (j > STAGE_POINT) {
            j = STAGE_POINT;
        }
        if (jj == password.length()) {
            j = j - 10;
        }
        points = points + j;

        // Non-Repeating characters - 8 recomend
        j = 0;
        for (int i = 0; i < password.length() - 1; i++) {
            if (!password.substring(i, i + 1).equals(password
                .substring(i + 1, i + 2))) {
                j = j + 2.86;
            }
        }
        if (j > STAGE_POINT) {
            j = STAGE_POINT;
        }
        points = points + j;

        points = points + getPairsOfMixedCase(a, password);
        points = points + getNonAlphaNumeric(an, password);
        return (points);
    }

    private static double getPairsOfMixedCase(String alphaLetters, String password) {
        double jj = 0;
        // upper case count
        int u = 0;
        // lower case count
        int l = 0;
        double j;

        // Pairs of mixed-case - 2 recomend
        for (int i = 0; i < password.length(); i++) {
            if (alphaLetters.contains(password.substring(i, i + 1))) {
                l++;
                jj++;
            }
            if (alphaLetters.toUpperCase().contains(password.substring(i, i + 1))) {
                u++;
                jj++;
            }
        }
        if (u > l) {
            j = l * 10D;
        } else {
            j = u * 10D;
        }
        if (j > STAGE_POINT) {
            j = STAGE_POINT;
        }
        if (jj == password.length()) {
            j = j - 10D;
        }
        return j;
    }

    private static double getNonAlphaNumeric(String alphaNumeric, String password) {
        // non-alpha-numeric characters - 2
        double j = 0;
        for (int i = 0; i < password.length(); i++) {
            if (j < STAGE_POINT) {
                if (!alphaNumeric.contains(password.substring(i, i + 1))) {
                    j = j + 10D;
                }
            }
        }// for
        if (j > STAGE_POINT) {
            j = STAGE_POINT;
        }
        return j;
    }

    /**
     * 生成随机密码
     *
     * @param pwdLen 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String getRandomNum(int pwdLen) {
        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i; // 生成的随机数
        // 生成的密码的长度
        int count = 0;
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'w',
            'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuilder pwd = new StringBuilder();

        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，
            // 生成的数最大为36-1
            i = Math.abs(RANDOM.nextInt(maxNum));

            if (i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }
}
