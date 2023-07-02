package com.netby.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

/**
 * 日期帮助类
 *
 * @author: byg
 */
@Slf4j
@SuppressWarnings("unused")
public class DateUtil {

    /**
     * 年月 <code>yyyy-MM</code>
     */
    public static final String MONTH_PATTERN = "yyyy-MM";

    /**
     * 年月日 <code>yyyyMMdd</code>
     */
    public static final String DEFAULT_PATTERN = "yyyyMMdd";

    /**
     * 年月日时分秒 <code>yyyyMMddHHmmss</code>
     */
    public static final String FULL_PATTERN = "yyyyMMddHHmmss";

    /**
     * 标准格式的年月日时分秒 <code>yyyyMMdd HH:mm:ss</code>
     */
    public static final String FULL_STANDARD_PATTERN = "yyyyMMdd HH:mm:ss";

    /**
     * 中文格式的年月日格式 <code>yyyy-MM-dd</code>
     */
    public static final String CHINESE_PATTERN = "yyyy-MM-dd";

    /**
     * 中文格式的年月日时分秒格式 <code>yyyy-MM-dd HH:mm:ss</code>
     */
    public static final String FULL_CHINESE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间增加
     *
     * @param date     原始时间
     * @param duration 幅度
     * @return 新的时间
     */
    public static Date dateAdd(Date date, Duration duration) {
        return new Date(date.getTime() + duration.toMillis());
    }

    /**
     * 时间减少
     *
     * @param date     原始时间
     * @param duration 幅度
     * @return 新的时间
     */
    public static Date dateDiff(Date date, Duration duration) {
        return new Date(date.getTime() - duration.toMillis());
    }

    /**
     * 把字符串转化成日期型。
     *
     * @param name         字符串
     * @param df           日期格式
     * @param defaultValue 默认值
     * @return 日期
     */
    public static Date getDate(String name, String df, Date defaultValue) {
        if (name == null) {
            return defaultValue;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(df);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        try {
            return formatter.parse(name);
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }

        return defaultValue;
    }

    public static Date getDateDf(String name, String df) {
        return getDate(name, df, null);
    }

    /**
     * 把字符串转化成日期型。 缺省为当前系统时间。
     *
     * @param name 字符串
     */
    public static Date getDate(String name) {
        return getDate(name, null);
    }

    /**
     * 把字符串转化成日期型。 缺省为当前系统时间。
     *
     * @param name 字符串
     */
    public static Date getDateTime(String name) {
        return getDateTime(name, null);
    }

    /**
     * 把字符串转化成日期型。
     *
     * @param name         字符串
     * @param defaultValue 日期格式
     * @return Date 转化后的日期
     */
    public static Date getDate(String name, Date defaultValue) {
        return getDate(name, "yyyy-MM-dd", defaultValue);
    }

    /**
     * 把字符串转化成日期型。
     *
     * @param name         字符串
     * @param defaultValue 日期格式
     * @return Date 转化后的日期
     */
    public static Date getDateTime(String name, Date defaultValue) {
        return getDate(name, "yyyy-MM-dd HH:mm:ss", defaultValue);
    }

    /**
     * @param str 要创建的标准时间格式
     * @return UNIX时间
     */
    public static int getUnixTimeByDateText(String str) {
        return parse(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param str 要创建的标准时间格式
     * @return 时间
     */
    public static Date parseToDate(String str, String format) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sd = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        return sd.parse(str, pos);
    }

    /**
     * @param str 要创建的标准时间格式
     * @return UNIX时间
     */
    public static int parse(String str, String format) {
        Date d1 = parseToDate(str, format);
        if (d1 == null) {
            return 0;
        }
        return (int) (d1.getTime() / 1000);
    }


    /**
     * 格式化为LocalDate
     *
     * @param text    文本
     * @param pattern 格式
     * @return LocalDate
     */
    public static LocalDate parseToLocalDate(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(text, formatter);
    }

    /**
     * 格式化为LocalDateTime
     *
     * @param text    文本
     * @param pattern 格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(text, formatter);
    }


    public static Date mysqlDate2Date(int seconds) {
        long l = (long) seconds * 1000;
        return new Date(l);
    }

    public static Date mysqlDate2Date(long seconds) {
        long l = seconds * 1000;
        return new Date(l);
    }

    public static long date2MysqlDate(Date date) {
        return date.getTime() / 1000;
    }

    public static int date2MysqlDateInt(Date date) {
        long l = date2MysqlDate(date);
        if (l > Integer.MAX_VALUE) {
            throw new RuntimeException("Unix time too long.");
        }
        return (int) l;
    }

    /**
     * 得到相差的天数
     *
     * @param begin Date
     * @param last  Date
     * @return 天
     */
    public static int getBetweenDays(Date begin, Date last) {
        LocalDate beginLocalDate = dateToLocalDate(begin);
        LocalDate lastLocalDate = dateToLocalDate(last);
        return Math.abs((int) (lastLocalDate.toEpochDay() - beginLocalDate.toEpochDay()));
    }

    /**
     * 得到相差的天数
     *
     * @param begin Date
     * @param last  Date
     * @return 天
     */
    public static int getBetweenDays(LocalDate begin, LocalDate last) {
        return Math.abs((int) (last.toEpochDay() - begin.toEpochDay()));
    }

    /**
     * 得到相差的小时
     *
     * @param begin Date
     * @param last  Date
     * @return 小时
     */
    public static long getBetweenHours(Date begin, Date last) {
        LocalDateTime beginLocalDate = dateToLocalDateTime(begin);
        LocalDateTime lastLocalDate = dateToLocalDateTime(last);
        Duration duration = Duration.between(beginLocalDate, lastLocalDate);
        return Math.abs(duration.toHours());
    }

    /**
     * 得到相差的分钟数
     *
     * @param begin Date
     * @param last  Date
     * @return 分钟
     */
    public static long getBetweenMinute(Date begin, Date last) {
        LocalDateTime beginLocalDate = dateToLocalDateTime(begin);
        LocalDateTime lastLocalDate = dateToLocalDateTime(last);
        Duration duration = Duration.between(beginLocalDate, lastLocalDate);
        return Math.abs(duration.toMinutes());
    }

    /**
     * 得到相差的秒
     *
     * @param begin Date
     * @param last  Date
     * @return 分钟
     */
    public static long getBetweenSeconds(Date begin, Date last) {
        LocalDateTime beginLocalDate = dateToLocalDateTime(begin);
        LocalDateTime lastLocalDate = dateToLocalDateTime(last);
        Duration duration = Duration.between(beginLocalDate, lastLocalDate);
        return Math.abs(duration.getSeconds());
    }

    /**
     * 得到相差的月数
     *
     * @param begin Date
     * @param last  Date
     * @return 月
     */
    public static int getBetweenMonths(Date begin, Date last) {
        LocalDate beginLocalDate = dateToLocalDate(begin);
        LocalDate lastLocalDate = dateToLocalDate(last);
        return Math.abs((lastLocalDate.getYear() - beginLocalDate.getYear()) * 12
            + lastLocalDate.getMonth().getValue() - beginLocalDate.getMonth().getValue());
    }

    /**
     * 得到相差的年数
     *
     * @param begin Date
     * @param last  Date
     * @return 月
     */
    public static int getBetweenYears(Date begin, Date last) {
        LocalDate beginLocalDate = dateToLocalDate(begin);
        LocalDate lastLocalDate = dateToLocalDate(last);
        return Math.abs(lastLocalDate.getYear() - beginLocalDate.getYear());
    }

    /**
     * 得到给定的时间距离现在的天数
     *
     * @param begin Date
     * @return 天数
     */
    public static int getBetweenDays(Date begin) {
        return getBetweenDays(begin, new Date());
    }

    /**
     * 可以使用美国时间格式化
     *
     * @param date   date
     * @param format 格式
     * @param useUsa 是否为美国时间
     * @return 格式化时间
     */
    public static String dateFormat(Date date, String format, boolean useUsa) {
        if (date != null) {
            SimpleDateFormat sdf;
            if (useUsa) {
                sdf = new SimpleDateFormat(
                    format, Locale.US);
            } else {
                sdf = new SimpleDateFormat(
                    format);
            }
            return sdf.format(date);
        } else {
            return null;
        }
    }

    /**
     * 格式化时间
     *
     * @param date   date
     * @param format 格式
     * @return 时间文本
     */
    public static String dateFormat(Date date, String format) {
        return dateFormat(date, format, false);
    }

    /**
     * 特殊格式化，距离当前时间越远精度越低
     *
     * @param seconds Unix时间
     * @return 时间文本
     */
    public static String dateFormatByHuman(int seconds) {
        Date date = DateUtil.mysqlDate2Date(seconds);

        return dateFormatByHuman(date);
    }

    /**
     * 特殊格式化，距离当前时间越远精度越低
     *
     * @param date 时间
     * @return 时间文本
     */
    public static String dateFormatByHuman(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        now.setTime(new Date());
        last.setTime(date);

        String format;
        if (now.get(Calendar.YEAR) == last.get(Calendar.YEAR)
            && now.get(Calendar.MONTH) == last.get(Calendar.MONTH)
            && now.get(Calendar.DATE) == last.get(Calendar.DATE)) {
            format = "H:mm";
        } else if (now.get(Calendar.YEAR) == last.get(Calendar.YEAR)) {
            format = "M月d日";
        } else {
            format = "yyyy年M月d日";
        }

        return dateFormat(date, format);
    }

    /**
     * 特殊时间显示，距离当前时间越远精度越低
     *
     * @param seconds unix时间
     * @return 时间文本
     */
    public static String getTimeBetweenByHuman(int seconds) {
        return getTimeBetweenByHuman(mysqlDate2Date(seconds));
    }

    /**
     * 特殊时间显示，距离当前时间越远精度越低
     *
     * @param date 时间
     * @return 时间文本
     */
    public static String getTimeBetweenByHuman(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        now.setTime(new Date());
        last.setTime(date);
        String re;
        long b;

        if (now.get(Calendar.YEAR) == last.get(Calendar.YEAR)
            && now.get(Calendar.MONTH) == last.get(Calendar.MONTH)
            && now.get(Calendar.DATE) == last.get(Calendar.DATE)
            && now.get(Calendar.HOUR_OF_DAY) == last
            .get(Calendar.HOUR_OF_DAY)) {
            b = getBetweenMinute(last.getTime(), now.getTime());
            re = "分钟";
        } else if (now.get(Calendar.YEAR) == last.get(Calendar.YEAR)
            && now.get(Calendar.MONTH) == last.get(Calendar.MONTH)
            && now.get(Calendar.DATE) == last.get(Calendar.DATE)) {
            b = getBetweenHours(last.getTime(), now.getTime());
            re = "小时";
        } else {
            b = getBetweenDays(last.getTime(), now.getTime());
            re = "天";
        }

        return b + re;
    }

    /**
     * unix时间格式化
     *
     * @param seconds unix时间
     * @param format  格式
     * @return 时间文本
     */
    public static String dateFormat(int seconds, String format) {
        Date date = DateUtil.mysqlDate2Date(seconds);
        return dateFormat(date, format);
    }

    /**
     * 清除日期中的小时，分钟，秒
     *
     * @param date Date
     * @return Date
     */
    public static Date clearHms(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime();
    }

    /**
     * 得到小时开始时间
     *
     * @return Date
     */
    public static Date getDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 得到小时结束时间
     *
     * @return Date
     */
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 得到小时开始时间
     *
     * @return Date
     */
    public static Date getHourStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 0, 0);
        return cal.getTime();
    }

    /**
     * 得到小时结束时间
     *
     * @return Date
     */
    public static Date getHourEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 59, 59);
        return cal.getTime();
    }

    /**
     * 得到月初时间
     *
     * @return Date
     */
    public static Date getMonthStart() {
        return getMonthStart(new Date());
    }

    /**
     * 得到月末时间
     *
     * @return Date
     */
    public static Date getMonthEnd() {
        return getMonthEnd(new Date());
    }

    /**
     * 得到月初时间
     *
     * @return Date
     */
    public static Date getMonthStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 得到月末时间
     *
     * @return Date
     */
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date转换为LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDate转换为Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 时间格式化
     *
     * @param localDate unix时间
     * @param pattern   格式
     * @return 时间文本
     */
    public static String dateFormat(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDate.atStartOfDay());
    }

    /**
     * 时间格式化
     *
     * @param localDateTime unix时间
     * @param pattern       格式
     * @return 时间文本
     */
    public static String dateFormat(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDateTime);
    }
}
