package com.netby.common.util;

/**
 * 版本比较工具类，只比较主版本.次版本.增量版本
 *
 * @author: byg
 * @since 2023/4/21
 */
public class VersionUtil {

    public static boolean aboveOrEqual(String version1, String baseVersion) {
        return compare(version1, baseVersion) >= 0;
    }

    public static boolean below(String version1, String baseVersion) {
        return compare(version1, baseVersion) == -1;
    }

    private static int compare(String version1, String version2) {
        String[] array1 = version1.split("\\.");
        String[] array2 = version2.split("\\.");
        int i = 0, j = 0;
        int len1 = Math.min(array1.length, 3), len2 = Math.min(array2.length, 3);
        while (i < len1 || j < len2) {
            int num1 = 0, num2 = 0;
            if (i < len1) {
                num1 = Integer.parseInt(array1[i]);
            }
            if (j < len2) {
                num2 = Integer.parseInt(array2[j]);
            }

            if (num1 > num2) {
                return 1;
            } else if (num1 < num2) {
                return -1;
            }
            i++;
            j++;
        }
        return 0;
    }
}

