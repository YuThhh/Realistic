package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Values {
    private final Map<UUID, Integer> thirsty = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Tag>> playerTags = new ConcurrentHashMap<>();

    public void setThirsty(UUID PlayerUUID, int thi) {
        thirsty.put(PlayerUUID, thi);
    }

    public int getThirsty(UUID PlayerUUID) {
        return thirsty.getOrDefault(PlayerUUID, 100);
    }

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
}
