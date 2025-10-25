package com.MaddyJace.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 玩家背包相关的工具类。
 * <p>
 * 提供与玩家背包相关的辅助方法，例如获取背包空格数量等。
 * </p>
 */
public class BukkitUtils {

    /**
     * 获取玩家主背包中的空格数量。
     * <p>
     * 仅统计主背包（槽位 0-35），不包括盔甲槽和副手槽。
     * 空格定义为该槽位的物品为 {@link Material#AIR} 或 {@code null}。
     * </p>
     *
     * @param player 要检查背包的 {@link Player} 对象
     * @return 玩家主背包中空格的数量
     */
    public static int getEmptySlots(Player player) {
        int emptySlots = 0;
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) emptySlots++;
        }
        return emptySlots;
    }

}
