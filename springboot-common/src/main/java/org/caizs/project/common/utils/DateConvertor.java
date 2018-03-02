package org.caizs.project.common.utils;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

/**
 * 
 * @ClassName: DateConvertor
 * @Description: 解析常见date字符串 获得 java.util.Date 对象
 *
 */
public class DateConvertor {

    public static final String WEB_TRANSFER_FORMAT = "yyyyMMddHHmmssSSS";
    private static final String WEB_TRANSFER_FORMAT_PATTERN = "^\\d{17}$";

    public static final String DATE_TIME_CN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";

    public static final String DATE_TIME_MILLI_CN = "yyyy-MM-dd HH:mm:ss:SSS";
    private static final String DATE_TIME_MILLI_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3}$";

    public static final String NO_SEC_CN = "yyyy-MM-dd HH:mm";
    private static final String NO_SEC_CN_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";

    public static final String DATE_TIME_EN = "dd/MM/yyyy HH:mm:ss";
    private static final String DATE_TIME_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$";

    public static final String DATE_TIME_MILLI_EN = "dd/MM/yyyy HH:mm:ss:SSS";
    private static final String DATE_TIME_MILLI_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}:\\d{3}$";

    public static final String NO_SEC_EN = "dd/MM/yyyy HH:mm";
    private static final String NO_SEC_EN_PATTERN = "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}$";

    public static final String DATE = "dd/MM/yyyy";
    private static final String DATE_PATTERN = "^\\d{2}/\\d{2}/\\d{4}$";

    private static final int YEAR_LOW = 1;

    // private static final int YEAR_TOP = 2200;

    private static final List<DateTimeFormatter> formats = Lists.newArrayList(DateTimeFormat.forPattern(WEB_TRANSFER_FORMAT),
            DateTimeFormat.forPattern(DATE_TIME_CN), DateTimeFormat.forPattern(DATE_TIME_MILLI_CN), DateTimeFormat.forPattern(NO_SEC_CN),
            DateTimeFormat.forPattern(DATE_TIME_EN), DateTimeFormat.forPattern(DATE_TIME_MILLI_EN), DateTimeFormat.forPattern(NO_SEC_EN),
            DateTimeFormat.forPattern(DATE));

    private static List<Pattern> regexPatterns = Lists.newArrayList(Pattern.compile(WEB_TRANSFER_FORMAT_PATTERN),
            Pattern.compile(DATE_TIME_CN_PATTERN), Pattern.compile(DATE_TIME_MILLI_CN_PATTERN), Pattern.compile(NO_SEC_CN_PATTERN),
            Pattern.compile(DATE_TIME_EN_PATTERN), Pattern.compile(DATE_TIME_MILLI_EN_PATTERN), Pattern.compile(NO_SEC_EN_PATTERN),
            Pattern.compile(DATE_PATTERN)

    );

    public static Date parseDate(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        int index = 0;
        boolean success = false;
        LocalDateTime date = null;
        while (index < formats.size()) {
            int currentIndex = index;
            ++index;
            Pattern pattern = regexPatterns.get(currentIndex);
            if (pattern.matcher(text).matches()) {
                date = formats.get(currentIndex).parseLocalDateTime(text);
                success = true;
                break;
            }
        }

        if (!success) {// 如果时间小于阈值，则直接取null
            date = LocalDateTime.parse(text);
        }

        if (date.getYear() <= YEAR_LOW) {// 如果时间小于阈值，则直接取null
            return null;
        }

        // if (date.getYear() >= YEAR_TOP) {//如果时间大于阈值，则直接报错
        // throw new IllegalArgumentException("Date string " + text + " is
        // illegal.");
        // }

        return date.toDate();
    }


}
