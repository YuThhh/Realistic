package org.role.realistic;

import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Damage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) {
            return;
        }

        double hp = Objects.requireNonNull(Objects.requireNonNull(p).getAttribute(Attribute.MAX_HEALTH)).getValue();
        if (event.getDamage() > hp / 4.0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 3));
        }

        if (event.getDamageSource().getDamageType() == DamageType.EXPLOSION) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 250, 5));
        }
    }
}
