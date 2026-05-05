package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Inventory implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        ItemStack clickedItem = e.getCurrentItem();

        // 아이템이 없거나 공기라면 무시
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;

        // 아이템 이름으로 "잠긴 보관함"인지 확인
        String displayName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());

        if (clickedItem.getType() == Material.BARRIER && displayName.contains("잠긴 보관함")) {
            e.setCancelled(true); // 클릭 취소
            p.updateInventory();
        }
    }
}
