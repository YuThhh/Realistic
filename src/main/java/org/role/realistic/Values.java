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
    private final Map<UUID, Integer> lastJump = new ConcurrentHashMap<>();
    private final Map<UUID, Double> playerTemp = new ConcurrentHashMap<>();
    private final Map<UUID, Double> tempTick = new ConcurrentHashMap<>();
    private final Map<UUID, Double> playerGrains = new ConcurrentHashMap<>();
    private final Map<UUID, Double> playerProteins = new ConcurrentHashMap<>();
    private final Map<UUID, Double> playerVegetables = new ConcurrentHashMap<>();
    private final Map<UUID, Double> playerSugar = new ConcurrentHashMap<>();

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
        playerTags.computeIfAbsent(uuid, _ -> new ConcurrentHashMap<>())
                .put(key, new Tag(name, color, duration, amplifier));
    }

    public Map<String, Tag> getTags(UUID uuid) {
        return playerTags.getOrDefault(uuid, Collections.emptyMap());
    }

    public void removeTag(UUID uuid, String key) {
        if (playerTags.containsKey(uuid)) playerTags.get(uuid).remove(key);
    }

    public void setLockInv(UUID uuid, int index, int value) {
        Integer[] gets = lockInv.computeIfAbsent(uuid, _ -> new Integer[]{0, 0, 0});
        gets[index] = value;
    }

    public Integer[] getLockedInv(UUID uuid) {
        return lockInv.getOrDefault(uuid, new Integer[]{0, 0, 0});
    }

    public void setLastJump(UUID uuid, int jump) {
        lastJump.put(uuid, jump);
    }

    public int getLastJump(UUID uuid) {
        return lastJump.getOrDefault(uuid, 0);
    }

    public void setPlayerTemp(UUID uuid, double temp) {playerTemp.put(uuid, temp);}

    public double getPlayerTemp(UUID uuid) {return playerTemp.getOrDefault(uuid, 25.0);}

    public void setTempTick(UUID uuid, double temp) {tempTick.put(uuid, temp);}

    public double getTempTick(UUID uuid) {return tempTick.getOrDefault(uuid, 0.0);}

    public void setPlayerGrains(UUID uuid, double grains) {playerGrains.put(uuid, grains);}

    public double getPlayerGrains(UUID uuid) {return playerGrains.getOrDefault(uuid, 100.0);}

    public void setPlayerProteins(UUID uuid, double proteins) {playerProteins.put(uuid, proteins);}

    public double getPlayerProteins(UUID uuid) {return playerProteins.getOrDefault(uuid, 100.0);}

    public void setPlayerVegetables(UUID uuid, double vegetable) {playerVegetables.put(uuid, vegetable);}

    public double getPlayerVegetables(UUID uuid) {return playerVegetables.getOrDefault(uuid, 100.0);}

    public void setPlayerSugar(UUID uuid, double sugar) {playerSugar.put(uuid, sugar);}

    public double getPlayerSugar(UUID uuid) {return playerSugar.getOrDefault(uuid, 100.0);}
}
