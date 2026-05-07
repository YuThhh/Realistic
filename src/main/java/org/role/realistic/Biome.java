package org.role.realistic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

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
                    double temp = transTemp(getTemperature(p));

                    if (temp < 0) {

                    } else if (temp < 25) {

                    } else if (temp < 35) {

                    }
                }
            }
        }.runTaskTimer(real, 0, 1);
    }
}
