package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.UUID;

public class Food extends Util implements Listener {

    public Food(Realistic real, Values values) {
        super(real, values);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Material item = e.getItem().getType();
        double currentThirsty = getThirsty(uuid);

        if (item == Material.POTION || item == Material.MILK_BUCKET) {
            setThirsty(uuid, currentThirsty + 45);
        } else if (item == Material.APPLE) {
            setThirsty(uuid, currentThirsty + 10);
        } else if (item == Material.MELON_SLICE) {
            setThirsty(uuid, currentThirsty + 15);
        } else if (item == Material.SWEET_BERRIES || item == Material.GLOW_BERRIES) {
            setThirsty(uuid, currentThirsty + 6);
        } else if (item == Material.GOLDEN_APPLE) {
            if (getTags(uuid).containsKey("infection")) {
                removeTag(uuid, "infection");
            }
        } else if (item == Material.ENCHANTED_GOLDEN_APPLE) {
            if (getTags(uuid).containsKey("infection")) {
                removeTag(uuid, "infection");
            } else if (getTags(uuid).containsKey("infection2")) {
                removeTag(uuid, "infection2");
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (e.getItem() == null) return;
        Material item = e.getItem().getType();

        if (e.getClickedBlock() == null) return;
        Block targetBlock = p.getTargetBlockExact(5);

        if (targetBlock == null) return;

        // TODO 다시 손봐야함 (물 클릭해도 회복 안됨)
        if (item == Material.AIR && p.isSneaking() && targetBlock.getType() == Material.WATER && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            double currentThirsty = getThirsty(uuid);
            setThirsty(uuid, currentThirsty + 10);
            if (Math.random() <= 0.5) {
                addTag(uuid, "intoxic", "중독", NamedTextColor.GREEN, 600, 1);
            }
        }

        if (item == Material.SNOWBALL && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "cooling", "시원함", NamedTextColor.BLUE, 600, 1);
        }
        if (item == Material.PACKED_ICE && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK ||  e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "cooling", "시원함", NamedTextColor.BLUE, 600, 3);
        }
        if ((item == Material.COAL || item == Material.CHARCOAL) && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "warming", "따뜻함", NamedTextColor.GOLD, 600, 1);
        }
    }
}
