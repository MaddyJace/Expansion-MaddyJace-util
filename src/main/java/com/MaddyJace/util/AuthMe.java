package com.MaddyJace.util;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.api.v3.AuthMePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * AuthMe 相关工具类。
 * <p>
 * 提供与 AuthMe 插件交互的辅助方法，例如获取玩家注册日期、注册状态，
 * 通过 IP 获取玩家列表和数量等。
 * </p>
 */
public class AuthMe {

    /**
     * 获取玩家注册日期，并按照指定格式返回字符串。
     * <p>
     * 日期格式遵循 {@link java.time.format.DateTimeFormatter} 的模式。
     * 如果获取失败或玩家不存在，则返回 "null"。
     * </p>
     *
     * @param player 要查询的 {@link Player} 对象
     * @param timeStr 日期格式化字符串，例如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的注册日期字符串，获取失败返回 "null"
     */
    public static String getRegistrationDate(Player player, String timeStr) {
        try {
            Optional<AuthMePlayer> authMePlayer = AuthMeApi.getInstance().getPlayerInfo(player.getName());
            if (authMePlayer.isPresent()) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(authMePlayer.get().getRegistrationDate(), ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeStr);
                return dateTime.format(formatter);
            }
            return "null";
        } catch (Exception e) {
            return "null";
        }
    }

    /**
     * 获取玩家注册到当前时间的时间差。
     * <p>
     * 可以指定返回单位：MILLI、SECOND、MINUTE、HOUR、DAY、MONTH、YEAR。
     * 如果获取失败或玩家不存在，则返回 0。
     * </p>
     *
     * @param player 要查询的 {@link Player} 对象
     * @param type 时间单位字符串，支持 "MILLI", "SECOND", "MINUTE", "HOUR", "DAY", "MONTH", "YEAR"
     * @return 注册时间到当前时间的时间差，单位由 type 指定，获取失败返回 -1
     */
    public static long getRegistrationDiffDate(Player player, String type) {
        try {
            Optional<AuthMePlayer> authMePlayer = AuthMeApi.getInstance().getPlayerInfo( player.getName() );
            if (authMePlayer.isPresent()) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(authMePlayer.get().getRegistrationDate(), ZoneId.systemDefault());
                return unitConversion(type, dateTime);
            } return -1;
        } catch (Exception e) {
            return -1;
        }
    }
    /**
     * 将起始时间与当前时间的差值转换为指定单位。
     *
     * @param unit 时间单位字符串
     * @param start 起始 {@link LocalDateTime}
     * @return 时间差值，如果单位不支持则返回 -1
     */
    private static long unitConversion(String unit, LocalDateTime start) {
        LocalDateTime end = LocalDateTime.now();
        switch (unit.toUpperCase(Locale.ROOT)) {
            case "MILLI":  return Duration.between(start, end).toMillis();
            case "SECOND": return Duration.between(start, end).getSeconds();
            case "MINUTE": return Duration.between(start, end).toMinutes();
            case "HOUR":   return Duration.between(start, end).toHours();
            case "DAY":    return ChronoUnit.DAYS.between(start, end);
            case "MONTH":  return ChronoUnit.MONTHS.between(start, end);
            case "YEAR":   return ChronoUnit.YEARS.between(start, end);
            default:       return -1;
        }
    }


    /**
     * 判断玩家是否已在 AuthMe 注册。
     *
     * @param player 要检查的 {@link Player} 对象
     * @return 如果玩家已注册返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isRegistered(Player player) {
        try {
            return AuthMeApi.getInstance().isRegistered(player.getName());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取使用过玩家所在 IP 的所有玩家名称，并以指定分隔符连接。
     * <p>
     * 如果获取失败或列表为空，则返回玩家自身的名称。
     * </p>
     *
     * @param player 玩家对象，用于获取 IP
     * @param input 名称连接符，例如 "," 或 " / "
     * @return 使用该 IP 的所有玩家名称字符串
     */
    public static String getNamesByIp(Player player, String input) {
        try {
            List<String> list = AuthMeApi.getInstance().getNamesByIp(player.getAddress().getAddress().getHostAddress());
            if (list == null || list.isEmpty()) return player.getName();
            return String.join(input, list);
        } catch (Exception e) {
            return player.getName();
        }
    }

    /**
     * 获取使用玩家所在 IP 的用户数量。
     * <p>
     * 如果获取失败或列表为空，则返回 1。
     * </p>
     *
     * @param player 玩家对象，用于获取 IP
     * @return 使用该 IP 的用户数量
     */
    public static int getUserCountByIp(Player player) {
        try {
            List<String> list = AuthMeApi.getInstance().getNamesByIp(player.getAddress().getAddress().getHostAddress());
            if(list == null || list.isEmpty()) return 1;
            return list.size();
        } catch (Exception e) {
            return 0;
        }
    }

}
