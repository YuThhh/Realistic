package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Food extends Util implements Listener {
    Set<Material> GRAINS = Set.of(Material.BREAD, Material.POTATO, Material.BAKED_POTATO, Material.PUMPKIN_PIE, Material.COOKIE);
    Set<Material> VEGETABLES = Set.of(Material.CARROT, Material.BEETROOT, Material.BEETROOT_SOUP, Material.GOLDEN_CARROT);
    Set<Material> SUGAR = Set.of(Material.APPLE, Material.MELON_SLICE, Material.SWEET_BERRIES, Material.GLOW_BERRIES, Material.HONEY_BOTTLE, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE, Material.PUMPKIN_PIE, Material.COOKIE, Material.CHORUS_FRUIT);

    public Food(Realistic real, Values values) {
        super(real, values);

        startNutrient();
    }

    public void startNutrient() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    double currentGrains = getPlayerGrains(uuid);
                    double currentProteins = getPlayerProteins(uuid);
                    double currentVegetables = getPlayerVegetables(uuid);
                    double currentSugar = getPlayerSugar(uuid);

                    // 기존 태그의 amplifier(레벨)를 읽어온다
                    int prevOver = 0;
                    int prevLack = 0;
                    if (getTags(uuid).containsKey("over_nutri")) {
                        prevOver = getTags(uuid).get("over_nutri").getAmplifier();
                    }
                    if (getTags(uuid).containsKey("lack_nutri")) {
                        prevLack = getTags(uuid).get("lack_nutri").getAmplifier();
                    }

                    // 새로운 누적값 계산 (기존 로직 유지하되 더 명확하게)
                    int newOver = 0;
                    int newLack = 0;
                    if (currentGrains > 150) newOver++;
                    else if (currentGrains < 15) newLack += 2;
                    else if (currentGrains < 45) newLack += 1;

                    if (currentProteins > 150) newOver++;
                    else if (currentProteins < 15) newLack += 2;
                    else if (currentProteins < 45) newLack += 1;

                    if (currentVegetables > 150) newOver++;
                    else if (currentVegetables < 15) newLack += 2;
                    else if (currentVegetables < 45) newLack += 1;

                    if (currentSugar > 150) newOver++;
                    else if (currentSugar < 15) newLack += 2;
                    else if (currentSugar < 45) newLack += 1;

                    // duration: 기존 코드가 2로 되어있는데 매우 짧음 -> 예: 200틱(약 10초) 권장
                    final int nutrientDuration = 200;

                    // over_nutri 처리: 변경이 있을 때만 addTag, 없으면 제거
                    if (newOver > 0) {
                        if (!getTags(uuid).containsKey("over_nutri") || prevOver != newOver) {
                            addTag(uuid, "over_nutri", "영양 과다", NamedTextColor.DARK_GREEN, nutrientDuration, newOver);
                        }
                    } else {
                        if (getTags(uuid).containsKey("over_nutri")) {
                            removeTag(uuid, "over_nutri");
                        }
                    }

                    // lack_nutri 처리: 변경이 있을 때만 addTag, 없으면 제거
                    if (newLack > 0) {
                        if (!getTags(uuid).containsKey("lack_nutri") || prevLack != newLack) {
                            addTag(uuid, "lack_nutri", "영양 부족", NamedTextColor.DARK_GREEN, nutrientDuration, newLack);
                        }
                    } else {
                        if (getTags(uuid).containsKey("lack_nutri")) {
                            removeTag(uuid, "lack_nutri");
                        }
                    }

                    if (currentGrains > 200) {setPlayerGrains(uuid, 200);}
                    else if (currentGrains <= 0) {setPlayerGrains(uuid, 0);}
                    if (currentProteins > 200) {setPlayerProteins(uuid, 200);}
                    else if (currentProteins <= 0) {setPlayerProteins(uuid, 0);}
                    if (currentVegetables > 200) {setPlayerVegetables(uuid, 200);}
                    else if (currentVegetables <= 0) {setPlayerVegetables(uuid, 0);}
                    if (currentSugar > 200) {setPlayerSugar(uuid, 200);}
                    else if (currentSugar <= 0) {setPlayerSugar(uuid, 0);}
                }
            }
        }.runTaskTimer(real, 0 ,1);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Material item = e.getItem().getType();
        double currentThirsty = getThirsty(uuid);
        double currentGrains = getPlayerGrains(uuid);
        double currentProteins = getPlayerProteins(uuid);
        double currentVegetables = getPlayerVegetables(uuid);
        double currentSugar = getPlayerSugar(uuid);

        if (Tag.ITEMS_MEAT.isTagged(item)) {
            setPlayerProteins(uuid, currentProteins + 10);
        } else if (GRAINS.contains(item)) {
            setPlayerGrains(uuid, currentGrains + 10);
        } else if (VEGETABLES.contains(item)) {
            setPlayerVegetables(uuid, currentVegetables + 10);
        } else if (SUGAR.contains(item)) {
            setPlayerSugar(uuid, currentSugar + 10);
         }

        if (item == Material.POTION || item == Material.MILK_BUCKET) {
            setThirsty(uuid, currentThirsty + 45);
        } else if (item == Material.APPLE) {
            setThirsty(uuid, currentThirsty + 10);
        } else if (item == Material.MELON_SLICE) {
            setThirsty(uuid, currentThirsty + 15);
        } else if (item == Material.SWEET_BERRIES || item == Material.GLOW_BERRIES) {
            setThirsty(uuid, currentThirsty + 6);
        } else if (item == Material.GOLDEN_APPLE) {
            if (getTags(uuid).containsKey("infection")) {
                removeTag(uuid, "infection");
            }
        } else if (item == Material.ENCHANTED_GOLDEN_APPLE) {
            if (getTags(uuid).containsKey("infection")) {
                removeTag(uuid, "infection");
            } else if (getTags(uuid).containsKey("infection2")) {
                removeTag(uuid, "infection2");
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (e.getItem() == null) return;
        Material item = e.getItem().getType();

        if (e.getClickedBlock() == null) return;
        Block targetBlock = p.getTargetBlockExact(5);

        if (targetBlock == null) return;

        // TODO 다시 손봐야함 (물 클릭해도 회복 안됨)
        if (item == Material.AIR && p.isSneaking() && targetBlock.getType() == Material.WATER && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            double currentThirsty = getThirsty(uuid);
            setThirsty(uuid, currentThirsty + 10);
            if (Math.random() <= 0.5) {
                addTag(uuid, "intoxic", "중독", NamedTextColor.GREEN, 600, 1);
            }
        }

        if (item == Material.SNOWBALL && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "cooling", "시원함", NamedTextColor.BLUE, 600, 1);
        }
        if (item == Material.PACKED_ICE && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK ||  e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "cooling", "시원함", NamedTextColor.BLUE, 600, 3);
        }
        if ((item == Material.COAL || item == Material.CHARCOAL) && p.isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            addTag(uuid, "warming", "따뜻함", NamedTextColor.GOLD, 600, 1);
        }
    }
}
