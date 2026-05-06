package org.role.realistic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Stamina extends Util implements Listener {

    public Stamina(Realistic real, Values values) {
        super(real, values);

        startStamina();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        int currentStamina = values.getStamina(uuid);

        setStamina(uuid, currentStamina - 1);
    }

    public void startStamina() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    if (getStamina(uuid) > 100) {
                        setStamina(uuid, 100);
                    } else if (getStamina(uuid) <= 0) {
                        setStamina(uuid, 0);
                    }


                }
            }
        }.runTaskTimer(real, 0 ,1);
    }
}
