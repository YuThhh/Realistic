package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Inventory extends Util implements Listener {

    public Inventory(Realistic real, Values values) {
        super(real, values);
    }

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
        String displayName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(meta.displayName()));

        if (clickedItem.getType() == Material.BARRIER && displayName.contains("잠긴 보관함")) {
            e.setCancelled(true); // 클릭 취소
            p.updateInventory();
        }
    }

    @EventHandler
    public void lockInv(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("잠긴 보관함", NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);

        for (int i = 9; i < 35; i++) {
            if(i <= 17 && getLockInv(uuid, 0) == 0) {
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("셜커 상자를 들고 F를 눌러 잠금 해제")
                        .decoration(TextDecoration.ITALIC, false));
                meta.lore(lore);
                item.setItemMeta(meta);
                p.getInventory().setItem(i, item);
            } else if (i <= 26 && getLockInv(uuid, 1) == 0) {
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("엔더 상자를 들고 F를 눌러 잠금 해제")
                        .decoration(TextDecoration.ITALIC, false));
                meta.lore(lore);
                item.setItemMeta(meta);
                p.getInventory().setItem(i, item);
            } else if (getLockInv(uuid, 2) == 0) {
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("구리 상자를 들고 F를 눌러 잠금 해제")
                        .decoration(TextDecoration.ITALIC, false));
                meta.lore(lore);
                item.setItemMeta(meta);
                p.getInventory().setItem(i, item);
            }
        }
    }

    @EventHandler
    public void unlockInv(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (p.getInventory().getItemInOffHand().getType() == Material.COPPER_CHEST && getLockInv(uuid, 0) == 0) {
            p.getInventory().setItemInOffHand(null);
            setLockInv(uuid, 0, 1);
        } else if (p.getInventory().getItemInOffHand().getType() == Material.ENDER_CHEST && getLockInv(uuid, 1) == 0) {
            p.getInventory().setItemInOffHand(null);
            setLockInv(uuid, 1, 1);
        } else if (p.getInventory().getItemInOffHand().getType() == Material.PINK_SHULKER_BOX && getLockInv(uuid, 2) == 0) {
            p.getInventory().setItemInOffHand(null);
            setLockInv(uuid, 2, 1);
        }

        if (getLockInv(uuid, 0) == 1) {
            for (int i = 9; i <= 17 ; i++) { p.getInventory().setItem(i, null); }
        } else if (getLockInv(uuid, 1) == 1) {
            for (int i = 18; i <= 26 ; i++) {p.getInventory().setItem(i, null);}
        } else if (getLockInv(uuid, 2) == 1) {
            for (int i = 27; i <= 35 ; i++) {p.getInventory().setItem(i, null);}
        }
    }
}
