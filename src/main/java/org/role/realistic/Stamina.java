package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
        double currentStamina = getStamina(uuid);

        setStamina(uuid, currentStamina - 1);

        setRegenTimeStamina(uuid, 30);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        double currentStamina = getStamina(uuid);

        if (p.isSprinting()) {
            setStamina(uuid, currentStamina - 0.2);
            setRegenTimeStamina(uuid, 30);
        }

        if (p.isJumping()) {
            setStamina(uuid, currentStamina - 1);
            setRegenTimeStamina(uuid, 30);
        }
    }

    public void startStamina() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    double currentStamina = getStamina(uuid);
                    int currentRegenTime = getRegenTimeStamina(uuid);

                    if (getRegenTimeStamina(uuid) <= 0) {
                        setRegenTimeStamina(uuid, 0);
                        setStamina(uuid, currentStamina + 1);
                    } if (getRegenTimeStamina(uuid) > 0) {
                        setRegenTimeStamina(uuid, currentRegenTime - 1);
                    }

                    if (getStamina(uuid) > 100) {
                        setStamina(uuid, 100);
                    } else if (getStamina(uuid) <= 0) {
                        setStamina(uuid, 0);
                        addTag(uuid, "exhaust", "탈진", NamedTextColor.YELLOW, 2 ,1);
                    }
                }
            }
        }.runTaskTimer(real, 0 ,1);
    }
}
