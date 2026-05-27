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

    public double getThirsty(UUID playerUUID) {
        return values.getThirsty(playerUUID);
    }

    public void setThirsty(UUID playerUUID, double thirsty) {
        values.setThirsty(playerUUID, thirsty);
    }

    public double getStamina(UUID playerUUID) {return values.getStamina(playerUUID);}

    public void setStamina(UUID playerUUID, double stamina) {values.setStamina(playerUUID, stamina);}

    public int getRegenTimeStamina(UUID playerUUID) {return values.getRegenTimeStamina(playerUUID);}

    public void setRegenTimeStamina(UUID playerUUID, int value) {values.setRegenTimeStamina(playerUUID, value);}

    public void addTag(UUID playerUUID,String key, String tag, NamedTextColor color, int duration, int amplifier) {
        values.addTag(playerUUID, key, tag, color, duration, amplifier);
    }

    public void removeTag(UUID playerUUID, String tag) {
        values.removeTag(playerUUID, tag);
    }

    public Map<String, Tag> getTags(UUID playerUUID) {
        return values.getTags(playerUUID);
    }

    public void setLockInv(UUID playerUUID, int index, int value) {values.setLockInv(playerUUID, index, value);}

    public Integer getLockInv(UUID playerUUID, int index) {return values.getLockedInv(playerUUID)[index];}

    public double getTemperature(Player p) {return p.getLocation().getBlock().getTemperature();}

    public double transTemp(double temp) {
        return 46.8 - 41.53 * Math.pow(0.479, temp);
    }

    public void setLastJump(UUID uuid, int jump) {values.setLastJump(uuid, jump);}

    public int getLastJump(UUID uuid) {return values.getLastJump(uuid);}

    public void setPlayerTemp(UUID uuid, double temp) {values.setPlayerTemp(uuid, temp);}

    public double getPlayerTemp(UUID uuid) {return values.getPlayerTemp(uuid);}

    public void setTempTick(UUID uuid, double tick) {values.setTempTick(uuid, tick);}

    public double getTempTick(UUID uuid) {return values.getTempTick(uuid);}

    public void setPlayerGrains(UUID uuid, double value) {values.setPlayerGrains(uuid, value);}

    public double getPlayerGrains(UUID uuid) {return values.getPlayerGrains(uuid);}

    public void setPlayerProteins(UUID uuid, double value) {values.setPlayerProteins(uuid, value);}

    public double getPlayerProteins(UUID uuid) {return values.getPlayerProteins(uuid);}

    public void setPlayerVegetables(UUID uuid, double value) {values.setPlayerVegetables(uuid, value);}

    public double getPlayerVegetables(UUID uuid) {return values.getPlayerVegetables(uuid);}

    public void setPlayerSugar(UUID uuid, double value) {values.setPlayerSugar(uuid, value);}

    public double getPlayerSugar(UUID uuid) {return values.getPlayerSugar(uuid);}
}
