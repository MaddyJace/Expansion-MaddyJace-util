package com.MaddyJace.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    /**
     * 获取玩家主手物品的材质类型
     */
    public static String getItemMaterial(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            return Material.AIR.name();
        }
        return item.getType().name();
    }

    /**
     * 获取玩家主手物品的数量
     */
    public static int getItemAmount(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) return 0;
        return item.getAmount();
    }

    /**
     * 获取玩家主手物品的本地化名称
     */
    public static String getItemLocalizedName(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            return "Air";
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLocalizedName()) {
            return meta.getLocalizedName();
        }
        return item.getType().name();
    }

    /**
     * 获取玩家主手物品的显示名称，如果没有重命名则显示本地化名称
     */
    public static String getItemDisplayName(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            return "Air";
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        // 没有自定义名称，则返回本地化名称
        return getItemLocalizedName(player);
    }

    /**
     * 判断玩家主手物品是否有附魔
     */
    public static boolean isItemEnchanted(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        return !item.getEnchantments().isEmpty();
    }

}
