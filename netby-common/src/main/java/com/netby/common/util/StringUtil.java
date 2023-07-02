package com.netby.common.util;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import com.netby.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: byg
 */
@Slf4j
@SuppressWarnings("unused")
public class StringUtil extends StringUtils {

    public interface Symbols {

        String COLON = ":";
        String COMMAS = ",";
        String AND = "&";
        String OR = "|";
        String HYPHEN = "-";
        String UNDER_LINE = "_";
        String STAR = "*";
        String POUND = "#";
        String DOLLAR = "$";
        String PERCENT = "%";
        String AT = "@";
        String EXCLAMATION = "!";
        String LEFT_BRACKET = "(";
        String RIGHT_BRACKET = ")";
        String EQUALS = "=";
        String PLUS = "+";
        String DOT = ".";
        String BACKSLASH = "\\";
        String SLASH = "/";
        String LEFT_BRACKETS = "[";
        String RIGHT_BRACKETS = "]";
        String LEFT_BRACES = "{";
        String RIGHT_BRACES = "}";
    }

    /**
     * url编码
     *
     * @param str str
     * @return str
     */
    public static String urlEncoder(String str) {
        return urlEncoder(str, StandardCharsets.UTF_8);
    }

    /**
     * 判断是否为空
     *
     * @param object obj
     * @return 是否
     */
    public static boolean isEmpty(final Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).isEmpty();
        }
        return false;
    }

    /**
     * url编码
     *
     * @param str     str
     * @param charset charset
     * @return str
     */
    public static String urlEncoder(String str, Charset charset) {
        if (isEmpty(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str, charset.name());
        } catch (UnsupportedEncodingException ignore) {
        }
        return "";
    }

    /**
     * url解码
     *
     * @param str str
     * @return str
     */
    public static String urlDecoder(String str) {
        return urlDecoder(str, StandardCharsets.UTF_8);
    }

    /**
     * url解码
     *
     * @param str     str
     * @param charset charset
     * @return str
     */
    public static String urlDecoder(String str, Charset charset) {
        if (isEmpty(str)) {
            return "";
        }
        try {
            return URLDecoder.decode(str, charset.name());
        } catch (UnsupportedEncodingException ignore) {

        }
        return "";
    }

    /**
     * 截取字符串
     *
     * @param content content
     * @param length  length
     * @return content
     */
    public static String showShort(String content, int length, String mark) {
        if (content == null) {
            return null;
        }
        if (content.getBytes().length <= length) {
            return content;
        }

        byte[] s = content.getBytes();
        int flag = 0;
        for (int i = 0; i < length; ++i) {
            if (s[i] < 0) {
                flag++;
            }
        }
        if (flag % 2 != 0) {
            length--;
        }

        byte[] d = new byte[length];
        System.arraycopy(s, 0, d, 0, length);
        return new String(d) + mark;
    }

    public static String showShort(String content, int length) {
        return showShort(content, length, "..");
    }

    /**
     * 编码BASE64值
     *
     * @param str str
     * @return str
     */
    public static String encodeBase64(String str) {
        if (str == null) {
            return null;
        }
        return encodeBase64(str.getBytes());
    }

    /**
     * 解码BASE64值
     *
     * @param str str
     * @return str
     */
    public static String decodeBase64(String str) {
        if (str == null) {
            return null;
        }
        return decodeBase64(str.getBytes());
    }

    /**
     * 编码BASE64值
     *
     * @param str str
     * @return str
     */
    public static String encodeBase64(byte[] str) {
        if (str == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(str);
    }

    /**
     * 解码BASE64值
     *
     * @param str str
     * @return str
     */
    public static String decodeBase64(byte[] str) {
        return new String(decodeBase64ToByte(str));
    }

    /**
     * 解码BASE64值
     *
     * @param str str
     * @return str
     */
    public static byte[] decodeBase64ToByte(String str) {
        if (str == null) {
            return null;
        }
        return decodeBase64ToByte(str.getBytes());
    }

    /**
     * 解码BASE64值
     *
     * @param str str
     * @return str
     */
    public static byte[] decodeBase64ToByte(byte[] str) {
        if (str == null) {
            return null;
        }
        return Base64.getDecoder().decode(str);
    }

    /**
     * 得到md5值
     *
     * @param str str
     * @return md5
     */
    public static String getMd5(String str) {
        return Md5Utils.getMd5(str);
    }

    /**
     * 获取十六进制字符串
     *
     * @param b 数据
     * @return string
     */
    public static String getHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte c : b) {
            int val = ((int) c) & 0xff;
            if (val < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 得到md5值
     *
     * @param plainText plainText
     * @return md5
     */
    public static String get16Md5(String plainText) {
        return Objects.requireNonNull(getMd5(plainText)).substring(8, 24);
    }


    /**
     * 解码QuotedPrintable字符串
     *
     * @param str str
     * @return str
     */
    public static String decodeQuotedPrintable(String str) {
        byte escapeChar = '=';
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b == escapeChar) {
                try {
                    int u = Character.digit((char) bytes[++i], 16);
                    int l = Character.digit((char) bytes[++i], 16);
                    if (u == -1 || l == -1) {
                        // 出错
                        return "";
                    }
                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    // 出错
                    return "";
                }
            } else {
                buffer.write(b);
            }
        }
        bytes = buffer.toByteArray();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 是否已某个字符串结尾
     */
    public static boolean endsWithIgnoreCase(String beChecked, String str) {
        if (beChecked == null || str == null) {
            return false;
        }
        return beChecked.toUpperCase().endsWith(str.toUpperCase());
    }

    /**
     * 截取字符串
     *
     * @param source source
     * @param start  start
     * @param end    end
     * @return str
     */
    public static String findStr(String source, String start, String end) {
        if (source == null) {
            return null;
        }
        int i;
        if (!isEmpty(start)) {
            i = source.indexOf(start);
            if (i < 0) {
                return null;
            }
            source = source.substring(i + start.length());
        }
        if (!isEmpty(end)) {
            i = source.indexOf(end);
            if (i < 0) {
                return null;
            }
            source = source.substring(0, i);
        }
        return source;
    }

    /**
     * 截取字符串
     *
     * @param source    source
     * @param lastStart lastStart
     * @param end       end
     * @return str
     */
    public static String findLastStr(String source, String lastStart, String end) {
        if (source == null) {
            return null;
        }
        int i;
        if (!isEmpty(lastStart)) {
            i = source.lastIndexOf(lastStart);
            if (i < 0) {
                return null;
            }
            source = source.substring(i + lastStart.length());
        }
        if (!isEmpty(end)) {
            i = source.indexOf(end);
            if (i < 0) {
                return null;
            }
            source = source.substring(0, i);
        }
        return source;
    }

    public static String join(String delimiter, String... list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() == 0) {
                sb.append(str);
            } else {
                if (!str.startsWith(delimiter)) {
                    sb.append(delimiter);
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 如果字符串为空，则抛出异常
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String requireNonBlank(String str) {
        if (isBlank(str)) {
            throw new BusinessException();
        }
        return str;
    }

    public static String requireNonBlank(String str, String message) {
        if (isBlank(str)) {
            throw new BusinessException(message);
        }
        return str;
    }

    /**
     * 驼峰分切
     *
     * @param camelName 名称
     * @param split     分切符号
     * @return 字符串
     */
    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName, 0, i);
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName : buf.toString();
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}