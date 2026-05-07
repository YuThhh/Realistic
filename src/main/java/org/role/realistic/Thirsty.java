package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Thirsty extends Util implements Listener {
    final int ticks = 20;
    public Thirsty(Realistic real, Values values) {
        super(real, values);
    }

    public void startThirsty() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()){
                    UUID uuid = p.getUniqueId();
                    double currentThirsty = getThirsty(uuid);

                    if (currentThirsty <= 0) {
                        addTag(uuid, "deadly_thirsty", "탈수", NamedTextColor.AQUA, ticks+1, 3);
                        p.damage(1);
                        addPotionEffect(p, PotionEffectType.MINING_FATIGUE, ticks, 3);
                        addPotionEffect(p, PotionEffectType.SLOWNESS, ticks, 4);
                    } else if (currentThirsty < 30) {
                        addTag(uuid, "insane_thirsty", "심각한 목마름", NamedTextColor.AQUA, ticks+1, 2);
                        addPotionEffect(p, PotionEffectType.MINING_FATIGUE, ticks, 2);
                        addPotionEffect(p, PotionEffectType.SLOWNESS, ticks, 2);
                    } else if (currentThirsty < 50) {
                        addTag(uuid, "thirsty", "목마름", NamedTextColor.AQUA, ticks+1, 1);
                        addPotionEffect(p, PotionEffectType.SLOWNESS, ticks, 1);
                    }

                    if (p.getGameMode() != GameMode.CREATIVE) {
                        setThirsty(uuid, currentThirsty - 0.1);
                    }

                    if (currentThirsty <= 0 && !(p.isDead())) {
                        setThirsty(uuid, 0);
                    } else if (currentThirsty > 100 && !(p.isDead())) {
                        setThirsty(uuid, 100);
                    }

                    if (p.isDead()) {
                        setThirsty(uuid, 100);
                    }
                }
            }
        }.runTaskTimer(real, 0, ticks);
    }

}
