package com.MaddyJace.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

/**
 * 时间计算工具类。
 * <p>
 * 提供多种基于当前时间的差值计算方法，例如距离指定时间、指定星期几、
 * 或指定月份日期的时间差。可返回不同时间单位的结果。
 * </p>
 *
 * <p>
 * 所有计算均使用系统默认时区（{@link ZoneId#systemDefault()}）。
 * </p>
 *
 * <p>
 * 支持的时间单位：
 * <ul>
 *     <li><b>MILLI</b>：毫秒</li>
 *     <li><b>SECOND</b>：秒</li>
 *     <li><b>MINUTE</b>：分钟</li>
 *     <li><b>HOUR</b>：小时</li>
 *     <li><b>DAY</b>：天</li>
 *     <li><b>MONTH</b>：月（按30天估算）</li>
 *     <li><b>YEAR</b>：年（按365天估算）</li>
 * </ul>
 * </p>
 */
public class TimeUtils {

    /** 私有构造方法，防止实例化 */
    private TimeUtils() {}

    /**
     * 计算距离当天（或次日）指定时间的时间差。
     * <p>
     * 例如：计算当前时间到今天 23:59:59 的剩余时间，或明天 08:00 的剩余时间。
     * </p>
     *
     * @param timeStr 目标时间字符串，格式为 {@code HH:mm:ss}
     * @param unit    返回的时间单位（MILLI、SECOND、MINUTE、HOUR、DAY、MONTH、YEAR）
     * @param isTomorrow 是否计算到明天的时间（true 表示次日）
     * @return 当前时间与目标时间的差值，单位由 {@code unit} 指定
     */
    public static long diffDays(String timeStr, String unit, boolean isTomorrow) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);

        // 解析目标时间
        LocalTime targetTime;
        try {
            targetTime = LocalTime.parse(timeStr);
        } catch (Exception e) {
            targetTime = LocalTime.parse("23:59:59");
        }
        LocalDate targetDate = isTomorrow ? LocalDate.now(zone).plusDays(1) : LocalDate.now(zone);

        LocalDateTime targetDateTime = LocalDateTime.of(targetDate, targetTime);

        // 如果是今天，且时间已经过去 -> 强制设为 23:59:59
        if (!isTomorrow && targetDateTime.isBefore(now)) {
            targetDateTime = LocalDateTime.of(LocalDate.now(zone), LocalTime.of(23, 59, 59));
        }

        // 计算时间差（毫秒）
        long diffMillis = Duration.between(now, targetDateTime).toMillis();
        return unitConversion(diffMillis, unit);
    }


    /**
     * 计算距离下一个指定星期几的时间差。
     * <p>
     * 例如：计算距离下一个星期五 18:00 的时间差。
     * </p>
     *
     * @param timeStr 目标时间字符串，格式为 {@code HH:mm:ss}
     * @param week    目标星期几（1=星期一，7=星期日）
     * @param unit    返回的时间单位（MILLI、SECOND、MINUTE、HOUR、DAY、MONTH、YEAR）
     * @return 当前时间与下一个目标星期几的时间差，单位由 {@code unit} 指定
     */
    public static long diffWeeks(String timeStr, int week, String unit) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);

        LocalTime targetTime;
        try {
            targetTime = LocalTime.parse(timeStr);
        } catch (Exception e) {
            targetTime = LocalTime.parse("23:59:59");
        }

        DayOfWeek targetDay = (week >= 1 && week <= 7) ? DayOfWeek.of(week) : DayOfWeek.MONDAY;
        LocalDate targetDate = LocalDate.now(zone).with(TemporalAdjusters.next(targetDay));


        LocalDateTime targetDateTime = LocalDateTime.of(targetDate, targetTime);
        long diffMillis = Duration.between(now, targetDateTime).toMillis();

        return unitConversion(diffMillis, unit);
    }

    /**
     * 计算距离下一个指定日期（每月几号）的时间差。
     * <p>
     * 例如：计算距离下个月 15 号 12:00 的时间差。
     * </p>
     *
     * @param timeStr    目标时间字符串，格式为 {@code HH:mm:ss}
     * @param dayOfMonth 目标日期（1~31），超出该月最大天数时自动取最大天
     * @param unit       返回的时间单位（MILLI、SECOND、MINUTE、HOUR、DAY、MONTH、YEAR）
     * @return 当前时间与下个月目标日期的时间差，单位由 {@code unit} 指定
     */
    public static long diffMonth(String timeStr, int dayOfMonth, String unit) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);

        LocalTime targetTime;
        try {
            targetTime = LocalTime.parse(timeStr);
        } catch (Exception e) {
            targetTime = LocalTime.parse("23:59:59");
        }

        LocalDate targetDate = LocalDate.now(zone).plusMonths(1);
        int maxDay = targetDate.lengthOfMonth();
        int safeDay = Math.min(dayOfMonth, maxDay);
        LocalDateTime targetDateTime = LocalDateTime.of(targetDate, targetTime).withDayOfMonth(safeDay);
        long diffMillis = Duration.between(now, targetDateTime).toMillis();
        return unitConversion(diffMillis, unit);
    }

    /**
     * 将毫秒数转换为指定时间单位。
     * <p>
     * 支持以下单位：
     * <b>MILLI, SECOND, MINUTE, HOUR, DAY, MONTH, YEAR</b>。
     * </p>
     *
     * @param millis 毫秒数
     * @param unit   目标单位字符串
     * @return 转换后的时间值，如果单位不支持则返回 -1
     */
    public static long unitConversion(long millis, String unit) {
        switch (unit.toUpperCase(Locale.ROOT)) {
            case "MILLI":  return millis;
            case "SECOND": return millis / 1000;
            case "MINUTE": return millis / (1000 * 60);
            case "HOUR":   return millis / (1000 * 60 * 60);
            case "DAY":    return millis / (1000L * 60 * 60 * 24);
            case "MONTH":  return millis / (1000L * 60 * 60 * 24 * 30);  // 按 30 天一个月估算
            case "YEAR":   return millis / (1000L * 60 * 60 * 24 * 365); // 按 365 天一年估算
            default:       return -1;
        }
    }

    public static String getTheWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return sdf.format(new Date());
    }

}
