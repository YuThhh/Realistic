package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Values {
    private final Map<UUID, Integer> thirsty = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> stamina = new ConcurrentHashMap<>();
    private final Map<UUID, Double> regenTimeStamina = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Tag>> playerTags = new ConcurrentHashMap<>();
    private final Map<UUID, Integer[]> lockInv = new ConcurrentHashMap<>();

    public void setThirsty(UUID PlayerUUID, int thi) {
        thirsty.put(PlayerUUID, thi);
    }

    public int getThirsty(UUID PlayerUUID) {
        return thirsty.getOrDefault(PlayerUUID, 100);
    }

    public void setStamina(UUID PlayerUUID, int value) {stamina.put(PlayerUUID, value);}

    public int getStamina(UUID PlayerUUID) {return stamina.getOrDefault(PlayerUUID, 100);}

    public void setRegenTimeStamina(UUID PlayerUUID, double value) {regenTimeStamina.put(PlayerUUID, value);}

    public double getRegenTimeStamina(UUID PlayerUUID) {return regenTimeStamina.getOrDefault(PlayerUUID, 1.0);}

    public void addTag(UUID uuid, String key, String name, NamedTextColor color, int duration) {
        playerTags.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(key, new Tag(name, color, duration));
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
}
