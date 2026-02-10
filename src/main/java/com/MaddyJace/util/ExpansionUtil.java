package com.MaddyJace.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PlaceholderAPI 扩展类 —— {@code %mut_*%}。
 * <p>
 * 本类用于注册自定义 PlaceholderAPI 占位符，统一以 {@code %mut_*%} 为前缀，
 * 支持多种功能模块，包括：
 * </p>
 *
 * <ul>
 *     <li><b>时间计算：</b>基于 {@link TimeUtils}，用于计算日期与时间差。</li>
 *     <li><b>AuthMe：</b>基于 {@link AuthMe} 工具类，用于查询玩家注册信息。</li>
 *     <li><b>Bukkit：</b>基于 {@link BukkitUtils}，用于获取玩家背包信息。</li>
 * </ul>
 *
 * <p>使用示例：</p>
 * <pre>
 *     ─ 时间计算（当天/次日）
 *     可用单位: milli, second, minute, hour, day, month, year
 *     %mut_diffDays.milli."HH:mm:ss".true%
 *     %mut_diffDays.second."HH:mm:ss".false%
 *
 *     ─ 计算到下个星期几
 *     可用单位: milli, second, minute, hour, day, month, year
 *     %mut_diffWeeks.minute."HH:mm:ss".5%
 *
 *     ─ 计算到下个月指定日期
 *     可用单位: milli, second, minute, hour, day, month, year
 *     %mut_diffMonths.hour."HH:mm:ss".15%
 *
 *     ─ 获取今天是星期几
 *     %mut_getTheWeek%
 *
 *     ─ AuthMe 插件相关
 *     %mut_authMe.registrationDate."yyyy-MM-dd HH:mm:ss"% # 获取注册的时间
 *     %mut_authMe.registrationDiffDate.day%               # 获取注册到现在的时间单位
 *     %mut_authMe.listNameByIp.","%                       # 获取已登录账号列表
 *     %mut_authMe.getUserCountByIp%                       # 获取已登录账户数量
 *     %mut_authMe.registered%                             # 是否已注册
 *
 *     ─ Bukkit API
 *     %mut_bukkit.emptySlots% # 背包空格(不包括装备栏和副手)
 *     %mut_bukkit.playerOnline.[playerName]% # 玩家在线
 *     %mut_bukkit.itemInHand%            主手物品材质
 *     %mut_bukkit.itemInHandName%       # 主手物品名称(本地化)
 *     %mut_bukkit.itemInHandCustomName% # 主手物品自定义名称(本地化)
 *     %mut_bukkit.itemInHandAmount%     # 主手物品数量
 *     %mut_bukkit.itemInHandEnchanted%  # 主手物品是否为附魔
 *
 *     - LuckPerms
 *     %mut_luckPermsExpiryTime."{}"% # 把LuckPerms过期时间解析为天(四舍五入)
 *
 * </pre>
 */
@SuppressWarnings("unused")
public class ExpansionUtil extends PlaceholderExpansion {

    /**
     * 获取该 Placeholder 的唯一标识符。
     * <p>占位符前缀为 {@code %mut_*%}。</p>
     *
     * @return 标识符字符串 "mut"
     */
    @Override
    public @NotNull String getIdentifier() {
        return "mut";
    }

    /**
     * 获取作者名称。
     *
     * @return 作者名 "MaddyJace"
     */
    @Override
    public @NotNull String getAuthor() {
        return "MaddyJace";
    }

    /**
     * 获取扩展版本号。
     *
     * @return 版本号 "1.0.0"
     */
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    /**
     * 当 Placeholder 被调用时执行。
     * <p>
     * 解析传入的占位符参数，并根据模块执行不同逻辑：
     * </p>
     * <ul>
     *     <li>{@code diffDays / diffWeeks / diffMonths}：调用 {@link TimeUtils}</li>
     *     <li>{@code authMe}：调用 {@link AuthMe}</li>
     *     <li>{@code bukkit}：调用 {@link BukkitUtils}</li>
     * </ul>
     *
     * <p>支持通过引号避免参数中 “.” 被错误分割。</p>
     *
     * @param player     请求该占位符的玩家对象，可为 {@code null}
     * @param identifier 占位符参数，例如 {@code diffDays.second."HH:mm:ss".true}
     * @return 替换结果字符串；若参数无效则返回提示信息
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        List<String> list = splitByDotIgnoreQuotes(identifier);

        switch (list.get(0).toUpperCase()) {
            case "DIFFDAYS":
                if (list.size() >= 4) {
                    return String.valueOf(TimeUtils.diffDays(list.get(2), list.get(1), Boolean.parseBoolean(list.get(3))));
                }
            case  "DIFFWEEKS":
                if (list.size() >= 4) {
                    int weeks;
                    try {
                        weeks = Integer.parseInt(list.get(3));
                    } catch (Exception e) {
                        weeks = 1;
                    }
                    return String.valueOf(TimeUtils.diffWeeks(list.get(2), weeks, list.get(1)));
                }
            case  "DIFFMONTHS":
                if (list.size() >= 4) {
                    int month;
                    try {
                        month = Integer.parseInt(list.get(3));
                    } catch (Exception e) {
                        month = 31;
                    }
                    return String.valueOf(TimeUtils.diffMonth(list.get(2), month, list.get(1)));
                }
            case "AUTHME":

                if (list.size() >= 2) {
                    if (list.get(1).equalsIgnoreCase("registered")) {
                        return AuthMe.isRegistered(player) ? "true" : "false";
                    }
                    if (list.get(1).equalsIgnoreCase("getUserCountByIp")) {
                        return String.valueOf(AuthMe.getUserCountByIp(player));
                    }
                }

                if (list.size() >= 3) {
                    if (list.get(1).equalsIgnoreCase("registrationDate")) {
                        return AuthMe.getRegistrationDate(player, list.get(2));
                    }
                    if (list.get(1).equalsIgnoreCase("registrationDiffDate")) {
                        return String.valueOf(AuthMe.getRegistrationDiffDate(player, list.get(2)));
                    }
                    if (list.get(1).equalsIgnoreCase("listNameByIp")) {
                        return AuthMe.getNamesByIp(player, String.valueOf(list.get(2)));
                    }
                }
            case "BUKKIT":
                if (list.size() >= 3 && list.get(1).equalsIgnoreCase("playerOnline")) {
                    return String.valueOf(Bukkit.getOnlinePlayers().contains(player));
                }
                if (list.size() >= 2) {
                    if (list.get(1).equalsIgnoreCase("emptySlots")) {
                        return String.valueOf(BukkitUtils.getEmptySlots(player));
                    }
                    if (list.get(1).equalsIgnoreCase("itemInHand")) {
                        return ItemUtils.getItemMaterial(player);
                    }
                    if (list.get(1).equalsIgnoreCase("itemInHandName")) {
                        return ItemUtils.getItemLocalizedName(player);
                    }
                    if (list.get(1).equalsIgnoreCase("itemInHandCustomName")) {
                        return ItemUtils.getItemDisplayName(player);
                    }
                    if (list.get(1).equalsIgnoreCase("itemInHandAmount")) {
                        return String.valueOf(ItemUtils.getItemAmount(player));
                    }
                    if (list.get(1).equalsIgnoreCase("itemInHandEnchanted")) {
                        return String.valueOf(ItemUtils.isItemEnchanted(player));
                    }
                }
            case "GETTHEWEEK":
                return TimeUtils.getTheWeek();

            case "LUCKPERMSEXPIRYTIME":
                if (list.size() >= 2) {
                    String str = PlaceholderAPI.setPlaceholders(player, list.get(1).replace("{", "%"));
                    return String.valueOf(DurationParser.parseToDays(str));
                }
                return "-1";
        }

        return "The parameter you entered does not exist.";
    }

    /**
     * 按点号分割字符串（忽略引号内的点）。
     * <p>
     * 用于解析占位符参数，例如：
     * <pre>
     * 输入: diffDays.second."HH:mm:ss".true
     * 输出: [diffDays, second, HH:mm:ss, true]
     * </pre>
     * </p>
     *
     * @param input 原始字符串
     * @return 分割后的字符串列表（已去除引号）
     */
    public static List<String> splitByDotIgnoreQuotes(String input) {
        String regex = "\\.(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        List<String> parts = new ArrayList<>();
        for (String part : input.split(regex)) {
            // 分割之后去掉首尾引号
            if (part.startsWith("\"") && part.endsWith("\"") && part.length() >= 2) {
                part = part.substring(1, part.length() - 1);
            }
            parts.add(part);
        }
        return parts;
    }

    /**
     * 解析占位符中内部的站位，递归解析从最里面开始！<p>
     */
    public static String parseAndReplace(String str, Player player) {
        Pattern pattern = Pattern.compile("(?<!\\\\)\\{([^{}]*)}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String inner = matcher.group(1); // 提取 {}
            String replacement = PlaceholderAPI.setPlaceholders(player, "%" + inner + "%");
            str = str.substring(0, matcher.start()) + replacement + str.substring(matcher.end());
            matcher = pattern.matcher(str);
        }
        str = str.replaceAll("\\\\([{}])", "$1");
        return str.replace("&", "§");
    }

}
