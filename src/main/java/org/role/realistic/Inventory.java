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
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
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
        // 1. 메타데이터가 없거나 이름이 아예 없으면 통과
        if (meta == null || !meta.hasDisplayName()) return;

        // 2. 이름이 있을 때만 안전하게 가져오기
        Component nameComponent = meta.displayName();
        if (nameComponent == null) return;

        String displayName = PlainTextComponentSerializer.plainText().serialize(nameComponent);

        // 3. 타입과 이름 체크
        if (clickedItem.getType() == Material.BARRIER && displayName.contains("잠긴 보관함")) {
            e.setCancelled(true);
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

        for (int i = 9; i <= 35; i++) {
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
    public void unlockInv(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack offHand = e.getOffHandItem();

        boolean changed = false;

        if (offHand.getType() == Material.COPPER_CHEST && getLockInv(uuid, 0) == 0) {
            e.setOffHandItem(null);
            changed = true;
            setLockInv(uuid, 0, 1);
            p.sendMessage(Component.text("1번째 줄 해금 완료"));
        } else if (offHand.getType() == Material.ENDER_CHEST && getLockInv(uuid, 1) == 0) {
            e.setOffHandItem(null);
            changed = true;
            setLockInv(uuid, 1, 1);
            p.sendMessage(Component.text("2번째 줄 해금 완료"));
        } else if (offHand.getType() == Material.SHULKER_BOX && getLockInv(uuid, 2) == 0) {
            e.setOffHandItem(null);
            changed = true;
            setLockInv(uuid, 2, 1);
            p.sendMessage(Component.text("3번째 줄 해금 완료"));
        }

        if (changed) {
            if (getLockInv(uuid, 2) == 1) {
                for (int i = 9; i <= 17; i++) {
                    p.getInventory().setItem(i, null);
                }
            } else if (getLockInv(uuid, 1) == 1) {
                for (int i = 18; i <= 26; i++) {
                    p.getInventory().setItem(i, null);
                }
            } else if (getLockInv(uuid, 0) == 1) {
                for (int i = 27; i <= 35; i++) {
                    p.getInventory().setItem(i, null);
                }
            }
        }
    }
}
