package org.caizs.project.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateTimeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

    public static final String PATTERN_A = "yyyy-MM-dd";
    public static final String PATTERN_A_1 = "yyyyMM";
    public static final String PATTERN_JQUERY = "dd/MM/yyyy";
    public static final String PATTERN_ZH = "yyyy年MM月dd日";
    public static final String PATTERN_B = "yyyyMMdd";
    public static final String PATTERN_C = "yyyy/MM/dd";
    public static final String PATTERN_D = "yyyyMM";
    public static final String PATTERN_E = "yyMM";
    public static final String PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_LONG_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_LONG_ZH = "yyyy年MM月dd日 HH时mm分ss秒";

    public static final String PATTERN_LONG_MS = "yyyyMMddHHmmssSSS";
    public static final String PATTERN_LONG_TIGHT = "yyyyMMddHHmmss";
    public static final String PATTERN_LONG_MINUTE_TIGHT = "yyyyMMddHHmm";

    public static final String HMS_START = " 00:00:00";
    public static final String HMS_END = " 23:59:59";

    public static final String PATTERN_LONG_SLASH = "yyyy/MM/dd HH:mm:ss";

    /**
     * 获得当前日期所在年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前日期所在年的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayInYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得当前日期所在年的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLasttDayInYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 计算两个日期间隔的小时数
     *
     * @param firstDate
     *            小者
     * @param lastDate
     *            大者
     * @return
     */
    public static int getTimeIntervalHours(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 60 * 1000));
    }

    /**
     * 获取日期对象,不包含时分秒
     *
     * @param day
     *            偏移的日期数,整数向前,负数向后
     * @return 不含时分秒的日期对象
     */
    public static Date getMidnightOfOneDay(int day) {
        Calendar midnight = Calendar.getInstance();
        midnight.add(Calendar.DAY_OF_MONTH, day);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return midnight.getTime();
    }

    /**
     * 描述： 获取日期特定格式字符串 作者： yangqiang 创建时间： 2009-9-8上午08:51:07
     *
     * @param date
     * @param datePattern
     *            字符串格式
     * @return
     * @since v0.1
     */
    public static String getDateStr(Date date, String datePattern) {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.format(date);
    }

    /**
     * 描述： 获取日期默认格式（"yyyy-MM-dd"）对应字符串 作者： yangqiang 创建时间： 2009-9-8上午08:51:11
     *
     * @param date
     * @return
     * @since v0.1
     */
    public static String getDateStr(Date date) {
        return getDateStr(date, DateTimeUtil.PATTERN_A);
    }

    /**
     * 描述： 将字符串按特定格式转换为日期 作者： yangqiang 创建时间： 2009-11-3下午01:27:34
     *
     * @param date
     * @param datePattern
     * @return
     * @throws ParseException
     * @since v2.0
     */
    public static Date parseDate(String date, String datePattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.parse(date);
    }

    /**
     * Parses text in 'YYYY-MM-DD' format to produce a date.
     *
     * @param s
     *            the text
     * @return Date
     * @throws ParseException
     * @throws ParseException
     */
    public static Date parseDate(String s) throws ParseException {
        return parseDate(s, DateTimeUtil.PATTERN_A);
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 描述： 获取某一时间的一个间隔时间 作者： yangqiang 创建时间： 2009-11-3下午01:28:21
     *
     * @intervalType 间隔类型 年 Calendar.YEAR、月Calendar.MONTH、日Calendar.DATE
     * @intervalVal 间隔长短
     * @curdate 基准时间
     * @since v2.0
     */
    public static String getIntervalDate(int intervalType, int intervalVal, Date curdate) {
        if (curdate == null) {
            curdate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curdate);
        switch (intervalType) {
            case Calendar.YEAR:
                calendar.add(Calendar.YEAR, intervalVal);
                return DateTimeUtil.getDateStr(calendar.getTime());
            case Calendar.MONTH:
                calendar.add(Calendar.MONTH, intervalVal);
                return DateTimeUtil.getDateStr(calendar.getTime());
            case Calendar.DATE:
                calendar.add(Calendar.DATE, intervalVal);
                return DateTimeUtil.getDateStr(calendar.getTime());
            default:
                calendar.add(Calendar.MONTH, intervalVal);
                return DateTimeUtil.getDateStr(calendar.getTime());
        }
    }

    /**
     * 描述： 获取特定时间所在年期<br/>
     * example：2009-11-15 为 200911<br/>
     * 作者： yangqiang<br/>
     * 创建时间： 2009-11-11下午03:30:24<br/>
     *
     * @param date
     *            <br/>
     * @since v2.0<br/>
     */
    public static int getYearPeriod(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取年期所在日历月的第一天，时间为 00:00:00 000。 如果时间小于2009年，则返回null
     *
     * @param yearPeriod
     * @return
     */
    public static Date yearPeriod2Date(int yearPeriod) {
        if (yearPeriod < 200900) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearPeriod / 100);
        cal.set(Calendar.MONTH, (yearPeriod % 100) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 描述： 判断是否为该月第一天 作者： yangqiang 创建时间： 2009-11-3下午02:06:37
     *
     * @param date
     * @return
     * @since v2.0
     */
    public static boolean isFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }

        return false;
    }

    /**
     * 描述： 判断是否为该月第一天 作者： yangqiang 创建时间： 2009-11-3下午02:06:37
     *
     * @param date
     * @return
     * @since v2.0
     */
    public static boolean isFirstDay(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }

        if (date.endsWith("-01")) {
            return true;
        }

        return false;
    }

    public static String getDateSub(Date date, String dateSubStr) {
        SimpleDateFormat format = new SimpleDateFormat(dateSubStr); // "yyyy"
        return format.format(date);
    }

    public static String getCurDateStr(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    public static Date parseDateC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.parse(s);
    }

    /**
     * Parses text in 'YYYY-MM-DD' format to produce a date.
     *
     * @param s
     *            the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseDateTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseDateTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.parse(s);
    }

    /**
     * Parses text in 'HH:mm:ss' format to produce a time.
     *
     * @param s
     *            the text
     * @return Date
     * @throws ParseException
     */
    public static Date parseTime(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.parse(s);
    }

    public static Date parseTimeC(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH时mm分ss秒");
        return format.parse(s);
    }

    public static int yearOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(0, 4));
    }

    public static int monthOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(5, 7));
    }

    public static int dayOfDate(Date s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(s);
        return Integer.parseInt(d.substring(8, 10));
    }

    @SuppressWarnings("deprecation")
    public static String getDateTimeStr(java.sql.Date date, double time) {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        String dateStr = year + "-" + month + "-" + day;
        Double d = new Double(time);
        String timeStr = String.valueOf(d.intValue()) + ":00:00";

        return dateStr + " " + timeStr;
    }

    /**
     * Get the total month from two date.
     *
     * @param sd
     *            the start date
     * @param ed
     *            the end date
     * @return int month form the start to end date
     * @throws ParseException
     */
    @SuppressWarnings("deprecation")
    public static int diffDateM(Date sd, Date ed) {
        return (ed.getYear() - sd.getYear()) * 12 + ed.getMonth() - sd.getMonth() + 1;
    }

    public static int diffDateD(Date sd, Date ed) {
        return Math.round((ed.getTime() - sd.getTime()) / 86400000) + 1;
    }

    public static int diffDateMinute(Date sd, Date ed) {
        return Math.round((ed.getTime() - sd.getTime()) / 60000);
    }

    public static int diffDateM(int sym, int eym) {
        return (int) ((Math.round((double) eym / 100) - Math.round((double) sym / 100)) * 12 + (eym % 100 - sym % 100) + 1);
    }

    public static java.sql.Date getNextMonthFirstDate(java.sql.Date date) throws ParseException {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.set(Calendar.DATE, 1);
        return new java.sql.Date(scalendar.getTime().getTime());
    }

    /**
     * 前或后几个月
     *
     * @param date
     * @param count
     * @return
     */
    public static Date getMonth(Date date, int count) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, count);
        return new Date(scalendar.getTime().getTime());
    }

    // public static java.sql.Date getFrontDateByDayCount(java.sql.Date date,
    // int dayCount) throws ParseException {
    // Calendar scalendar = new GregorianCalendar();
    // scalendar.setTime(date);
    // scalendar.add(Calendar.DATE, -dayCount);
    // return new java.sql.Date(scalendar.getTime().getTime());
    // }

    public static Date getFrontDateByDayCount(Date date, int dayCount) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.DATE, -dayCount);
        return new Date(scalendar.getTime().getTime());
    }

    /**
     * Get first day of the month.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @return Date first day of the month.
     * @throws ParseException
     */
    public static Date getFirstDay(String year, String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(year + "-" + month + "-1");
    }

    public static Date firstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    // 取所在月下一月第一天
    public static Date firstDayOfNextMonth() {
        Calendar calendar = Calendar.getInstance();
        return firstDayOfNextMonth(calendar.getTime());
    }

    public static Date firstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 取今天所在月的第一天。
     *
     * @return
     * @author hewei
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(Calendar.getInstance().getTime());
    }

    /**
     * 取今天所在月的最后一天
     */
    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(Calendar.getInstance().getTime());
    }

    /**
     * 取当天所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date day) {
        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(day);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.set(Calendar.DATE, 1);
        Date firstDayOfNextMonth = new Date(scalendar.getTime().getTime());
        return getDateBefore(firstDayOfNextMonth, 1);
    }

    /**
     * 取day所在月的第一天
     *
     * @param day
     * @return
     * @author hewei
     */
    public static Date getFirstDayOfMonth(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getFirstDay(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date rtn = null;
        try {
            rtn = format.parse(year + "-" + month + "-1");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return rtn;
    }

    public static Date getLastDay(String year, String month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(year + "-" + month + "-1");

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    public static Date getLastDay(int year, int month) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(year + "-" + month + "-1");

        Calendar scalendar = new GregorianCalendar();
        scalendar.setTime(date);
        scalendar.add(Calendar.MONTH, 1);
        scalendar.add(Calendar.DATE, -1);
        date = scalendar.getTime();
        return date;
    }

    /**
     * getToday get todat string with format YYYY-MM-DD from a Date object
     *
     * @param date
     *            date
     * @return String
     */

    // public static String getTodayStr() throws ParseException {
    // Calendar calendar = Calendar.getInstance();
    // return getDateStr(calendar.getTime());
    // }
    public static Date getToday() {
        return new Date(System.currentTimeMillis());
    }

    public static final Date tuncateMillis(Date date) {
        return new Date(tuncateMillis(date.getTime()));
    }

    public static final void tuncateMillis(Calendar calendar) {
        calendar.setTimeInMillis(tuncateMillis(calendar.getTimeInMillis()));
    }

    public static final long tuncateMillis(long millis) {
        return (millis / 1000L * 1000L);
    }

    public static final Date toDate(Long millis) {
        if (millis == null)
            return null;

        return new Date(tuncateMillis(millis.longValue()));
    }

    public static String getTodayAndTime() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    // public static String getTodayC() throws ParseException {
    // Calendar calendar = Calendar.getInstance();
    // return getDateStrC(calendar.getTime());
    // }

    public static int getThisYearMonth() {
        Date today = Calendar.getInstance().getTime();
        return getYearPeriod(today);
    }

    public static int getYearMonth(Date date) {
        return getYearPeriod(date);
    }

    // 获取相隔月数
    @SuppressWarnings("deprecation")
    public static long getDistinceMonth(String beforedate, String afterdate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long monthCount = 0;
        try {
            java.util.Date d1 = d.parse(beforedate);
            java.util.Date d2 = d.parse(afterdate);

            monthCount = (d2.getYear() - d1.getYear()) * 12 + d2.getMonth() - d1.getMonth();
            // dayCount = (d2.getTime()-d1.getTime())/(30*24*60*60*1000);

        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return monthCount;
    }

    // 获取相隔天数
    public static long getDistinceDay(String beforedate, String afterdate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        long dayCount = 0;
        try {
            java.util.Date d1 = d.parse(beforedate);
            java.util.Date d2 = d.parse(afterdate);

            dayCount = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    /**
     * 忽略小时直接的差异,只比较日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
    }

    // 获取相隔天数
    public static long getDistinceDay(Date beforedate, Date afterdate) {
        long dayCount = 0;

        try {
            dayCount = (afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    public static long getDistinceDay(java.sql.Date beforedate, java.sql.Date afterdate) {
        long dayCount = 0;

        try {
            dayCount = (afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    public static long getDistinceDay(long beforedateTime, long afterdateTime) {
        long dayCount = 0;

        try {
            dayCount = (beforedateTime - afterdateTime) / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return dayCount;
    }

    // 获取相隔时间数
    public static long getDistinceTime(String beforeDateTime, String afterDateTime) throws ParseException {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long timeCount = 0;
        try {
            java.util.Date d1 = d.parse(beforeDateTime);
            java.util.Date d2 = d.parse(afterDateTime);

            timeCount = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000);

        } catch (ParseException e) {
            LOGGER.error("", e);
            throw e;
        }
        return timeCount;
    }

    // 获取相隔时间数
    @SuppressWarnings("deprecation")
    public static long getDistinceTime(String beforeDateTime) throws ParseException {
        return getDistinceTime(beforeDateTime, new Timestamp(System.currentTimeMillis()).toLocaleString());
    }

    /**
     * 把cal的时间设置为 00:00:00 000
     *
     * @param cal
     */
    public static void clearTimeFiled(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 把cal的时间设置为 23:59:59 000
     *
     * @param cal
     */
    public static void setLastTimeFiled(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        clearTimeFiled(c);
        return getDateStr(c.getTime(), PATTERN_LONG);
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        setLastTimeFiled(c);
        return getDateStr(c.getTime(), PATTERN_LONG);
    }

    /**
     * 描述： 获得上周星期日的日期 <br/>
     * 作者： yangqiang <br/>
     * 创建时间： 2010-7-16下午02:35:19 <br/>
     *
     * @param date
     *            为空 表示当前日期
     * @return
     * @since v2.0 <br/>
     */
    public static String getPreviousWeekSunday(Date date) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        if (date != null) {
            currentDate.setTime(date);
        }
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        setLastTimeFiled(currentDate);

        return getDateStr(currentDate.getTime(), PATTERN_LONG);
    }

    /**
     * 描述：获得上周星期一的日期 <br/>
     * 作者： yangqiang <br/>
     * 创建时间： 2010-7-16下午02:35:48 <br/>
     *
     * @param date
     *            为空 表示当前日期
     * @return
     * @since v2.0 <br/>
     */
    public static String getPreviousWeekday(Date date) {
        int weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        if (date != null) {
            currentDate.setTime(date);
        }
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        clearTimeFiled(currentDate);

        return getDateStr(currentDate.getTime(), PATTERN_LONG);
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 日期添加指定天数
     *
     * @param day
     * @param dayCount
     * @return
     */
    public static Date AddDate(Date day, int dayCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_MONTH, dayCount);
        return calendar.getTime();
    }

    public static long getUnixTimeStamp(int dateAdd, int field) {
        Calendar c = Calendar.getInstance();
        c.add(field, dateAdd);
        return Math.round(c.getTime().getTime() / 1000);
    }

    public static long getUnixTimeStamp() {
        return Math.round(new Date().getTime() / 1000);
    }

    /**
     * Unix时间搓转换成Stirng时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateStrByUnix(long date, String pattern) {
        return getDateStr(new Date(date * 1000), pattern);
    }

    /**
     * 取得指定日期的结束点，值为yyyy-MM-dd 23:59:59:999，如2011-09-07的结束时间为2011-09-07
     * 23:59:59:999
     *
     * @param d
     *            待操作的日期
     * @return 指定的日期的结束时间点
     */
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    // 当天 00:00:00
    public static Date getDayBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获取相隔年数
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
                || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(date);
        return cal;
    }

    /**
     * 判断指定的日期是否属于同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null && d2 == null)
            return true;
        if (d1 == null && d2 != null)
            return false;
        if (d1 != null && d2 == null)
            return false;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 删除某日期中的秒和毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateSecond(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    /**
     * 删除某日期中的毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateMillisecond(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
