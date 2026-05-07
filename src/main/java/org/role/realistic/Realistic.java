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
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, tags.get("thirsty").getDuration(), 1, false, false));
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
