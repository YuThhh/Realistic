package org.role.realistic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (e.getItem() == null) return;
        Material item = e.getItem().getType();

        if (e.getClickedBlock() == null) return;

        if (item == Material.AIR && p.isSneaking() && e.getClickedBlock().getType() == Material.WATER && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            double currentThirsty = getThirsty(uuid);
            setThirsty(uuid, currentThirsty + 20);
        }


    }
}
