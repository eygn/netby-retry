package com.netby.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV工具类
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class CsvUtil {

    public static String[] buildCsvArray(String param) {
        return buildCsvArray(param, ',');
    }

    private static final char EMPTY_STR = ' ';

    /**
     * build array(String) with csv format
     *
     * @param param param
     * @return String[]
     */
    public static String[] buildCsvArray(String param, char comma) {
        List<String> list = new ArrayList<>();
        int len = param.length();
        char quot = '"';
        boolean csvMode;
        // start and end with double quotation

        // abc, ,, "n/a, n/a", "", "abc"
        int i = 0;
        while (i < len) {
            char key = param.charAt(i);
            if (quot == key) {
                csvMode = true;
                i++;
            } else {
                csvMode = false;
            }
            if (i < len) {
                if (csvMode) {
                    i = processingCsv(list, param, comma, i);
                } else {
                    i = processingWord(list, param, comma, i);
                }
            }
        }
        // add text if last char is comma, example: "a,b," -> "a", "b", ""
        if (param.length() > 0) {
            if (comma == param.charAt(len - 1)) {
                list.add("");
            }
        }

        return list.toArray(new String[0]);

    }

    /**
     * process the space between work and work, and return next work start index
     *
     * @param param csv string "abc,    def"
     * @param i     the previous word end index, sample: 4
     * @return the next work start index, samele: 8
     */
    private static int processClose(String param, int i) {
        int len = param.length();
        if (i >= len) {
            return i;
        }
        char c = param.charAt(i);
        while (EMPTY_STR == c) {
            i++;
            c = param.charAt(i);
        }
        return i;
    }

    private static int processingCsv(List<String> list, String param, char comma, int i) {
        StringBuilder sb = new StringBuilder();
        int len = param.length();
        if (i + 1 >= len) {
            list.add(sb.toString());
            return i;
        }
        char c = param.charAt(i);
        char cc = param.charAt(i + 1);
        char quot = '"';
        while (c != quot || cc != comma) {
            if (c == quot && cc == quot) {
                i++;
            }
            sb.append(c);
            c = param.charAt(++i);
            if (i + 1 >= len) {
                break;
            }
            cc = param.charAt(i + 1);
        }
        list.add(sb.toString());
        i = processClose(param, i + 2);
        return i;
    }

    private static int processingWord(List<String> list, String param, char comma, int i) {
        StringBuilder sb = new StringBuilder();
        int len = param.length();
        if (i >= len) {
            list.add(sb.toString());
            return i;
        }
        for (; i < len; i++) {
            char c = param.charAt(i);
            if (c != comma) {
                sb.append(c);
            } else {
                break;
            }
        }
        list.add(sb.toString());
        i = processClose(param, ++i);
        return i;
    }

}
