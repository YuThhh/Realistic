package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Realistic extends JavaPlugin {
    Values value = new Values();
    Thirsty thirsty = new Thirsty(this, value);
    Util util = new Util(this, value);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Damage(this, value), this);
        getServer().getPluginManager().registerEvents(new Food(this, value), this);
        getServer().getPluginManager().registerEvents(new Inventory(this, value), this);
        getServer().getPluginManager().registerEvents(new Stamina(this, value), this);
        Objects.requireNonNull(getCommand("thirsty")).setExecutor(new Cmd(this, value));
        Objects.requireNonNull(getCommand("bleed")).setExecutor(new Cmd(this, value));
        thirsty.startThirsty();

        startCheckTag();
        startActionBar();
    }

    public void startActionBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    int currentThirsty = value.getThirsty(uuid);
                    int currentStamina = value.getStamina(uuid);
                    Map<String, Tag> tags = value.getTags(uuid);

                    List<String> toRemove = new ArrayList<>();

                    for (Map.Entry<String, Tag> entry : tags.entrySet()) {
                        entry.getValue().decreaseDuration(1); // 1초(20틱)씩 감소[cite: 6]
                        if (entry.getValue().getDuration() <= 0) {
                            toRemove.add(entry.getKey());
                        }
                    }

                    // 만료된 태그 제거
                    for (String tagName : toRemove) {
                        value.removeTag(uuid, tagName);
                    }

                    Component tagComponent = Component.empty();

                    for (Map.Entry<String, Tag> entry : tags.entrySet()) {
                        Component eachTag = Component.text(" [" +entry.getValue().getName()+"]", entry.getValue().getColor());

                        tagComponent = tagComponent.append(eachTag);
                    }

                    Component message = Component.text("갈증: " + currentThirsty, NamedTextColor.BLUE)
                            .append(Component.text(" 기력: " + currentStamina, NamedTextColor.YELLOW))
                            .append(tagComponent);

                    p.sendActionBar(message);

                }
            }
        }.runTaskTimer(this, 0L,1L);
    }

    public void startCheckTag() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    Map<String, Tag> tags = value.getTags(uuid);

                    if (tags.containsKey("thirsty")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("thirsty").getDuration(), tags.get("thirsty").getAmplifier());
                    } else if (tags.containsKey("insane_thirsty")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("insane_thirsty").getDuration(), tags.get("insane_thirsty").getAmplifier());
                        util.addPotionEffect(p, PotionEffectType.MINING_FATIGUE, tags.get("insane_thirsty").getDuration(), tags.get("insane_thirsty").getAmplifier());
                    } else if (tags.containsKey("deadly_thirsty")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("deadly_thirsty").getDuration(), tags.get("deadly_thirsty").getAmplifier());
                        util.addPotionEffect(p, PotionEffectType.MINING_FATIGUE, tags.get("deadly_thirsty").getDuration(), tags.get("deadly_thirsty").getAmplifier()-1);
                    }

                    if (tags.containsKey("exhaust")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("exhaust").getDuration(), tags.get("exhaust").getAmplifier());
                    }

                    if (tags.containsKey("broken")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("broken").getDuration(), tags.get("broken").getAmplifier());
                    } else if (tags.containsKey("insane_broken")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("insane_broken").getDuration(), tags.get("insane_broken").getAmplifier());
                    }

                    if (tags.containsKey("weak_concussion")) {
                        util.addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("weak_concussion").getDuration(), tags.get("weak_concussion").getAmplifier());
                    } else if (tags.containsKey("concussion")) {
                        util.addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("concussion").getDuration(), tags.get("concussion").getAmplifier());
                    } else if (tags.containsKey("insane_concussion")) {
                        util.addPotionEffect(p, PotionEffectType.NAUSEA, tags.get("insane_concussion").getDuration(), tags.get("insane_concussion").getAmplifier());
                    }

                    if (tags.containsKey("shock")) {
                        util.addPotionEffect(p, PotionEffectType.SLOWNESS, tags.get("shock").getDuration(), tags.get("shock").getAmplifier());
                        util.addPotionEffect(p, PotionEffectType.BLINDNESS, tags.get("shock").getDuration(), tags.get("shock").getAmplifier()-1);
                    }
                }
            }
        }.runTaskTimer(this,0L,1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
