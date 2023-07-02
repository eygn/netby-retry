package com.netby.common.util;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class IpUtil {

    /**
     * IP字段通配符
     */
    private static final String DEFAULT_IP_WILDCARD = "*";
    /**
     * IPv4最大字段数
     */
    private static final int IP_FIELD_CNT = 4;
    /**
     * IP范围切分字符
     */
    private static final String IP_RANGE_SPLIT_STR = "-";
    /**
     * IP字段最小值
     */
    private static final int IP_FIELD_MIN_VALUE = 0;
    /**
     * IP字段最大值
     */
    private static final int IP_FIELD_MAX_VALUE = 255;
    /**
     * IP转成长整形后的最小值
     */
    private static final long IP_MIN_LONG_VALUE = 0L;
    /**
     * P转成长整形后的最大值
     */
    private static final long IP_MAX_LONG_VALUE = 4294967295L;

    public static long ipv4ToLong(String ipv4) {
        String[] octets = ipv4.split("\\.");
        if (IP_FIELD_CNT != octets.length) {
            throw new IllegalArgumentException("IPv4 invalid!");
        }
        long ip = 0;
        for (int i = IP_FIELD_CNT - 1; i >= 0; i--) {
            final long octet = Long.parseLong(octets[3 - i]);
            if (IP_FIELD_MIN_VALUE > octet || IP_FIELD_MAX_VALUE < octet) {
                throw new IllegalArgumentException("IPv4 invalid!");
            }
            ip |= octet << (i * 8);
        }
        return ip;
    }

    public static String longToIpv4(long ipv4) {
        if (IP_MIN_LONG_VALUE > ipv4 || IP_MAX_LONG_VALUE < ipv4) {
            // if ip is out of range 0.0.0.0~255.255.255.255
            throw new IllegalArgumentException("IPv4 invalid!");
        }
        final StringBuilder ipAddress = new StringBuilder(15);
        for (int i = IP_FIELD_CNT - 1; i >= 0; i--) {
            final int shift = i * 8;
            ipAddress.append((ipv4 & (0xFFL << shift)) >> shift);
            if (i > 0) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    /**
     * 最后的*
     *
     * <pre>
     * e.g.
     *      192.168.*
     *      192.168.12*
     *      192.168.3.*
     *      192.168.3.1*
     * </pre>
     *
     * @param ipv4WithWildcard 要计算的ipv4的长整形值
     * @return 范围数组
     */
    public static long[] calcLowAndHigh(String ipv4WithWildcard) {
        return calcLowAndHigh(ipv4WithWildcard, DEFAULT_IP_WILDCARD);
    }

    /**
     * 假设最后的通配符为*
     *
     * <pre>
     * e.g.
     *      192.168.*
     *      192.168.12*
     *      192.168.3.*
     *      192.168.3.1*
     * </pre>
     *
     * @param ipv4WithWildcard 要计算的ipv4的长整形值
     * @param wildcard         通配符
     * @return 范围数组
     */
    public static long[] calcLowAndHigh(String ipv4WithWildcard, final String wildcard) {

        String[] octets = ipv4WithWildcard.split("\\.");
        if (IP_FIELD_CNT < octets.length) {
            throw new IllegalArgumentException("IPv4 invalid!");
        }
        long minIp = 0;
        long maxIp = 0;
        long ip = 0;
        for (int i = IP_FIELD_CNT - 1; i >= IP_FIELD_CNT - octets.length; i--) {
            String field = octets[3 - i];
            if (field.endsWith(wildcard)) {
                long[] pair = calcFieldLowAndHigh(field, wildcard);
                minIp = ip | (pair[0] << (i * 8));
                maxIp = ip | (pair[1] << (i * 8));
                break; // 结束
            }
            final long octet = Long.parseLong(field);
            if (IP_FIELD_MIN_VALUE > octet || IP_FIELD_MAX_VALUE < octet) {
                throw new IllegalArgumentException("IPv4 invalid!");
            }
            ip |= octet << (i * 8);
        }
        return new long[]{minIp, maxIp};
    }

    private static long[] calcFieldLowAndHigh(String ipv4FieldWithWildcard, final String wildcard) {

        long minIp = 0;
        long maxIp;
        int index = ipv4FieldWithWildcard.indexOf(wildcard);
        switch (index) {
            // 只有通配符
            case 0:
                maxIp = IP_FIELD_MAX_VALUE;
                break;
            // 有一位前缀
            case 1:
                final int prefix = Integer.parseInt(ipv4FieldWithWildcard.substring(0, index));
                switch (prefix) {
                    case 1:
                        minIp = prefix * 10L;
                        maxIp = prefix * 100L + 99L;
                        break;
                    case 2:
                        minIp = prefix * 10L;
                        maxIp = prefix * 100L + 55L;
                        break;
                    default:
                        minIp = prefix * 10L;
                        maxIp = prefix * 10L + 9L;
                        break;
                }
                break;
            case 2:
                final int triple = Integer.parseInt(ipv4FieldWithWildcard.substring(0, index));
                if (triple < 25) {
                    maxIp = triple * 10L + 9L;
                } else if (triple == 25) {
                    maxIp = triple * 10L + 5L;
                } else {
                    throw new IllegalArgumentException("IPv4 field invalid!");
                }
                minIp = triple * 10L;
                break;
            default:
                throw new IllegalArgumentException("IPv4 field invalid!");
        }

        return new long[]{minIp, maxIp};
    }

    /**
     * 校验所给IP是否满足给定规则
     *
     * @param ipStr     待校验IP
     * @param ipPattern IP匹配规则。支持单个ip、 * 匹配和 - 匹配范围。用分号分隔; 例如：192.168.1.*;192.168.1.1-128
     * @return bool
     */
    public static boolean validateIp(String ipStr, String ipPattern) {
        if (ipStr == null) {
            return false;
        }
        String[] patternList = ipPattern.split(";");
        for (String pattern : patternList) {
            if (passValidate(ipStr, pattern)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        boolean fl = IpUtil.validateIp("192.168.32.14", "192.168.*.1-100;192.168.34.*");
        System.out.println(fl);
    }

    private static boolean passValidate(String ipStr, String pattern) {
        String[] ipStrArr = ipStr.split("\\.");
        String[] patternArr = pattern.split("\\.");
        if (ipStrArr.length != IP_FIELD_CNT || patternArr.length != IP_FIELD_CNT) {
            return false;
        }
        int end = ipStrArr.length;
        if (patternArr[IP_FIELD_CNT - 1].contains(IP_RANGE_SPLIT_STR)) {
            end = 3;
            String[] rangeArr = patternArr[3].split(IP_RANGE_SPLIT_STR);
            int from = Integer.parseInt(rangeArr[0]);
            int to = Integer.parseInt(rangeArr[1]);
            int value = Integer.parseInt(ipStrArr[3]);
            if (value < from || value > to) {
                return false;
            }
        }
        for (int i = 0; i < end; i++) {
            if (patternArr[i].equals(DEFAULT_IP_WILDCARD)) {
                continue;
            }
            if (!patternArr[i].equalsIgnoreCase(ipStrArr[i])) {
                return false;
            }
        }
        return true;
    }
}
