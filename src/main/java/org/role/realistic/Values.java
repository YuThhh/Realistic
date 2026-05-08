package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Values {
    private final Map<UUID, Double> thirsty = new ConcurrentHashMap<>();
    private final Map<UUID, Double> stamina = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> regenTimeStamina = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Tag>> playerTags = new ConcurrentHashMap<>();
    private final Map<UUID, Integer[]> lockInv = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> lastjump = new ConcurrentHashMap<>();
    private final Map<UUID, Double> lastRun = new ConcurrentHashMap<>();

    public void setThirsty(UUID PlayerUUID, double thi) {
        thirsty.put(PlayerUUID, thi);
    }

    public double getThirsty(UUID PlayerUUID) {
        return thirsty.getOrDefault(PlayerUUID, 100.0);
    }

    public void setStamina(UUID PlayerUUID, double value) {stamina.put(PlayerUUID, value);}

    public double getStamina(UUID PlayerUUID) {return stamina.getOrDefault(PlayerUUID, 100.0);}

    public void setRegenTimeStamina(UUID PlayerUUID, int value) {regenTimeStamina.put(PlayerUUID, value);}

    public int getRegenTimeStamina(UUID PlayerUUID) {return regenTimeStamina.getOrDefault(PlayerUUID, 1);}

    public void addTag(UUID uuid, String key, String name, NamedTextColor color, int duration, int amplifier) {
        playerTags.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(key, new Tag(name, color, duration, amplifier));
    }

    public Map<String, Tag> getTags(UUID uuid) {
        return playerTags.getOrDefault(uuid, Collections.emptyMap());
    }

    public void removeTag(UUID uuid, String key) {
        if (playerTags.containsKey(uuid)) playerTags.get(uuid).remove(key);
    }

    public void setLockInv(UUID uuid, int index, int value) {
        Integer[] gets = lockInv.computeIfAbsent(uuid, k -> new Integer[]{0, 0, 0});
        gets[index] = value;
    }

    public Integer[] getLockedInv(UUID uuid) {
        return lockInv.getOrDefault(uuid, new Integer[]{0, 0, 0});
    }

    public void setLastjump(UUID uuid, int jump) {
        lastjump.put(uuid, jump);
    }

    public int getLastJump(UUID uuid) {
        return lastjump.getOrDefault(uuid, 0);
    }

    public void setLastRun(UUID uuid, double run) {lastRun.put(uuid, run);}

    public double getLastRun(UUID uuid) {return lastRun.getOrDefault(uuid, 0.0);}
}
