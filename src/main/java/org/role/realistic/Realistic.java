package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Realistic extends JavaPlugin implements Listener {
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
        getServer().getPluginManager().registerEvents(new Environment(this, value), this);
        getServer().getPluginManager().registerEvents(new TagManager(this, value), this);
        Objects.requireNonNull(getCommand("thirsty")).setExecutor(new Cmd(this, value));
        Objects.requireNonNull(getCommand("bleed")).setExecutor(new Cmd(this, value));
        Objects.requireNonNull(getCommand("temp")).setExecutor(new Cmd(this, value));
        thirsty.startThirsty();
        startActionBar();
        setRecipe();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.getAttribute(Attribute.MAX_HEALTH) != null) {
            Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(40);
        }

    }

    public void startActionBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    double currentThirsty = value.getThirsty(uuid);
                    double currentStamina = value.getStamina(uuid);
                    double currentTemp = util.getPlayerTemp(uuid);
                    double currentNeutri = (util.getPlayerGrains(uuid) + util.getPlayerProteins(uuid) + util.getPlayerVegetables(uuid) + util.getPlayerSugar(uuid))/4.0;
                    Map<String, Tag> tags = value.getTags(uuid);

                    // 태그 지속 시간 감소 및 만료된 태그 제거
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

                    Component message = Component.text("갈증: " + (int) currentThirsty, NamedTextColor.BLUE)
                            .append(Component.text(" 기력: " + (int) currentStamina, NamedTextColor.YELLOW))
                            .append(Component.text(" 온도: " + String.format("%.1f", currentTemp), NamedTextColor.GOLD))
                            .append(Component.text("영상 상태: " + (int) currentNeutri, NamedTextColor.GOLD))
                            .append(tagComponent);

                    p.sendActionBar(message);

                }
            }
        }.runTaskTimer(this, 0L,1L);
    }

    public void setRecipe() {
        Bukkit.resetRecipes();
        NamespacedKey key = new NamespacedKey("minecraft", "golden_apple");
        Bukkit.removeRecipe(key);
        NamespacedKey key2 = new NamespacedKey(this, "golden_apple");
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ShapedRecipe recipe = new ShapedRecipe(key2, item);
        recipe.shape("ABA", "ACA", "AAA");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.DIAMOND);
        recipe.setIngredient('C', Material.APPLE);
        Bukkit.addRecipe(recipe);
        NamespacedKey key3 = new NamespacedKey(this, "enchanted_golden_apple");
        ItemStack item2 = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ShapedRecipe recipe2 = new ShapedRecipe(key3, item2);
        recipe2.shape("ABA", "ACA", "AAA");
        recipe2.setIngredient('A', Material.GOLD_BLOCK);
        recipe2.setIngredient('B', Material.DIAMOND);
        recipe2.setIngredient('C', Material.APPLE);
        Bukkit.addRecipe(recipe2);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
