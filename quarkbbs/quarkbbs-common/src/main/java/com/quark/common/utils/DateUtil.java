package com.quark.common.utils;



import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Classname:DateUtil
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2021-05-18 11:59
 * @Version: 1.0
 **/
public class DateUtil {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * @Description :将日期转为时间格式字符串
     * @Date 12:02 2021/5/18 0018
     * @Param * @param dateTime 时间对象
     * @param pattern ：格式
     * @return java.lang.String
     **/
    public  static String dateToString(LocalDateTime dateTime,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeFormatter.format(dateTime);
    }
    public  static String dateToString(LocalDateTime dateTime){
     return  dateToString(dateTime,DEFAULT_PATTERN);
    }
    /**
     * @Description :将日期格式的字符串转为指定格式的日期对象
     * @Date 12:05 2021/5/18 0018
     * @Param * @param dateStr
     * @param pattern ：
     * @return java.time.LocalDateTime
     **/
    public static LocalDateTime  dateStrTodateTime(String dateStr,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return (LocalDateTime) dateTimeFormatter.parse(dateStr);
    }
    /**
     * @Description :将日期格式的字符串转为指定格式的日期对象
     * @Date 12:05 2021/5/18 0018
     * @Param * @param dateStr
     * @return java.time.LocalDateTime
     **/
    public static LocalDateTime  dateStrTodateTime(String dateStr){
       return dateStrTodateTime(dateStr,DEFAULT_PATTERN);
    }
}
