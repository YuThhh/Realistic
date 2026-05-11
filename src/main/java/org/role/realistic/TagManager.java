package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class TagManager extends Util implements Listener {
    public TagManager(Realistic real, Values values) {
        super(real, values);

        startCheckTag();
    }

    public void startCheckTag() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    Map<String, Tag> tags = getTags(uuid);

                    if (tags.containsKey("thirsty")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("thirsty").getDuration(), tags.get("thirsty").getAmplifier());
                    } else if (tags.containsKey("insane_thirsty")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("insane_thirsty").getDuration(), tags.get("insane_thirsty").getAmplifier());
                        addPotionEffect(p, PotionEffectType.MINING_FATIGUE, tags.get("insane_thirsty").getDuration(), tags.get("insane_thirsty").getAmplifier());
                    } else if (tags.containsKey("deadly_thirsty")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("deadly_thirsty").getDuration(), tags.get("deadly_thirsty").getAmplifier());
                        addPotionEffect(p, PotionEffectType.MINING_FATIGUE, tags.get("deadly_thirsty").getDuration(), tags.get("deadly_thirsty").getAmplifier()-1);
                    }

                    if (tags.containsKey("exhaust")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("exhaust").getDuration(), tags.get("exhaust").getAmplifier());
                    }

                    if (tags.containsKey("broken")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("broken").getDuration(), tags.get("broken").getAmplifier());
                    } else if (tags.containsKey("insane_broken")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("insane_broken").getDuration(), tags.get("insane_broken").getAmplifier());
                    }

                    if (tags.containsKey("weak_concussion")) {
                        addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("weak_concussion").getDuration(), tags.get("weak_concussion").getAmplifier());
                    } else if (tags.containsKey("concussion")) {
                        addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("concussion").getDuration(), tags.get("concussion").getAmplifier());
                    } else if (tags.containsKey("insane_concussion")) {
                        addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("insane_concussion").getDuration(), tags.get("insane_concussion").getAmplifier());
                    }

                    if (tags.containsKey("shock")) {
                        addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("shock").getDuration(), tags.get("shock").getAmplifier());
                        addPotionEffect(p, PotionEffectType.BLINDNESS, tags.get("shock").getDuration(), tags.get("shock").getAmplifier()-1);
                    }

                    if (tags.containsKey("cold")) {
                        setTempTick(uuid, getTempTick(uuid) +1);
                        if (getTempTick(uuid) > 1200) {
                            removeTag(uuid, "cold");
                            addTag(uuid, "frost", "저체온증", NamedTextColor.AQUA, 600, 1);
                        }
                    } else if (tags.containsKey("hot")) {
                        setTempTick(uuid, getTempTick(uuid) +1);
                        if (getTempTick(uuid) > 1200) {
                            removeTag(uuid, "hot");
                            addTag(uuid, "heat", "열사병", NamedTextColor.RED, 600, 1);
                        }
                    } else {setTempTick(uuid, getTempTick(uuid) - 1);}

                    if (getTempTick(uuid) <= 0) {
                        setTempTick(uuid, 0);
                    }
                }
            }
        }.runTaskTimer(real,0L,1L);
    }
}
