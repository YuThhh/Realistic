package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Util{
    protected Realistic real;
    protected Values values;

    public Util(Realistic real, Values values) {
        this.real = real;
        this.values = values;
    }

    public void addPotionEffect(Player p, PotionEffectType type, int du, int amp) {
        p.addPotionEffect(new PotionEffect(type, du, amp, true, false, false));
    }

    public int getThirsty(UUID playerUUID) {
        return values.getThirsty(playerUUID);
    }

    public void setThirsty(UUID playerUUID, int thirsty) {
        values.setThirsty(playerUUID, thirsty);
    }

    public void addTag(UUID playerUUID,String key, String tag, NamedTextColor color, int duration) {
        values.addTag(playerUUID, key, tag, color, duration);
    }

    public void removeTag(UUID playerUUID, String tag) {
        values.removeTag(playerUUID, tag);
    }

    public Map<String, Tag> getTags(UUID playerUUID) {
        return values.getTags(playerUUID);
    }

    public void setLockInv(UUID playerUUID, int index, int value) {values.setLockInv(playerUUID, index, value);}

    public Integer getLockInv(UUID playerUUID, int index) {return values.getLockedInv(playerUUID)[index];}
}
