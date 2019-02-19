package com.gelin.activitispringboot.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Auther 葛林
 * @Date 2019/2/1/001 11
 * @Remarks  时间工具类
 */
public class DateUtils {

    //常用的

    public static String getLocalDateTimeByYYYYMMDD() {
        return getDateTimeToStringByType("yyyy-MM-dd");
    }

    public static String getLocalDateTimeByYYYYMMDDHHmmss() {
        return getDateTimeToStringByType("yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDate parseLocalDateByYYYYMMDD(String date){
        return parseLocalDate(date,"yyyy-MM-dd");
    }

    public static LocalDateTime parseLocalDateTimeByYYYYMMDDHHmmss(String date){
        return parseLocalDateTime(date,"yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 根据传入的格式化类型，得到时间字符串
     * @param pattern  时间格式
     * @return
     */
    public static String getDateTimeToStringByType(String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.now();
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 根据传入的日期类型，将日期字符串转化为日期类型
     * @param date     日期字符串，格式与日期格式相同
     * @param pattern  日期格式
     * @return
     */
    public static LocalDate parseLocalDate(String date,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date,dateTimeFormatter);
    }


    /**
     * 根据传入的时间类型，将时间字符串转化为时间类型
     * @param dateTime 时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateTime,String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTime,dateTimeFormatter);
    }


    /**
     * 得到当前的年份
     * @return
     */
    public static int getYear(){
        return LocalDate.now().getYear();
    }


    /**
     * 得到当月月份
     * @return
     */
    public static int getMonth(){
        return LocalDate.now().getMonthValue();
    }

    /**
     * 得到当天
     * @return
     */
    public static int getDay(){
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * 得到当前秒时间戳
     * @return
     */
    public static long getCurrentTimestamp(){
        return Instant.now().getEpochSecond();
    }


}
