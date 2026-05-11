package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Biome extends Util implements Listener {
    public Biome(Realistic real, Values values) {
        super(real, values);

        startCheckBiome();
    }

    public void startCheckBiome() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    double temp = transTemp(getTemperature(p));
                    double playerTemp = getPlayerTemp(uuid);
                    double deltaTemp = (playerTemp - temp)*0.0005;

                    if (playerTemp != temp) {
                        setPlayerTemp(uuid, playerTemp - deltaTemp);
                    }

                    if (playerTemp <= 5) {
                        addTag(uuid, "cold", "추위", NamedTextColor.AQUA, 2, 1);
                    } else if (playerTemp >= 28) {
                        addTag(uuid, "hot", "더위", NamedTextColor.RED, 2, 1);
                    }
                }
            }
        }.runTaskTimer(real, 0, 1);
    }
}
