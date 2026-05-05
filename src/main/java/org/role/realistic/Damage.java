package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Damage extends Util implements Listener {

    public Damage(Realistic real, Values values) {
        super(real, values);
        bleed();
    }

    public void bleed() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    Map<String, Tag> tags = values.getTags(uuid);
                    if (tags.containsKey("bleed")) {
                        p.damage(Math.random()*1.7);
                    }
                }
            }
        }.runTaskTimer(real, 0, 4);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }

        DamageType damageCause = event.getDamageSource().getDamageType();
        UUID uuid = p.getUniqueId();
        double finalDamage = event.getFinalDamage();
        double hp = Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).getValue();
        if (finalDamage > hp / 4.0) {
            addTag(uuid, "shock", "쇼크", NamedTextColor.YELLOW, 100);
            addPotionEffect(p, PotionEffectType.SLOWNESS, 100, 3);
            addPotionEffect(p, PotionEffectType.BLINDNESS, 100, 1);
        }

        if (damageCause == DamageType.ARROW || damageCause == DamageType.TRIDENT) {
            if (Math.random() < 0.33) { // 더 직관적인 확률 계산
                addTag(uuid, "bleed", "출혈", NamedTextColor.RED, 50);
            }
        }

        if (damageCause == DamageType.FALL) {
            if (finalDamage > hp / 2.0) {
                addTag(uuid, "insane_broken", "심각한 골절", NamedTextColor.WHITE, 300);
                addPotionEffect(p, PotionEffectType.SLOWNESS, 300, 5);
            } else if (finalDamage > hp / 3.0) {
                addTag(uuid, "broken", "골절", NamedTextColor.WHITE, 200);
                addPotionEffect(p, PotionEffectType.SLOWNESS, 200, 2);
            } else if (finalDamage > hp / 4.0) {
                if (Math.random() < 0.5) {
                    addTag(uuid, "broken", "골절", NamedTextColor.WHITE, 100);
                    addPotionEffect(p, PotionEffectType.SLOWNESS, 100, 2);
                }
            }
        }

        if (damageCause == DamageType.EXPLOSION || damageCause == DamageType.PLAYER_EXPLOSION) {
            if (finalDamage > hp / 2.0) {
                addTag(uuid, "insane_concussion", "심각한 뇌진탕", NamedTextColor.YELLOW, 500);
                addPotionEffect(p, PotionEffectType.NAUSEA, 100, 2);
            } else if (finalDamage > hp / 6.0) {
                addTag(uuid, "concussion", "뇌진탕", NamedTextColor.YELLOW, 250);
                addPotionEffect(p, PotionEffectType.NAUSEA, 250, 3);
            } else if (finalDamage > hp / 10.0) {
                addTag(uuid, "weak_concussion", "약한 뇌진탕", NamedTextColor.YELLOW, 100);
                addPotionEffect(p, PotionEffectType.NAUSEA, 500, 5);
            }
        }
    }
}
