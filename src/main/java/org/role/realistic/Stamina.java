package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class Stamina extends Util implements Listener {

    public Stamina(Realistic real, Values values) {
        super(real, values);

        startStamina();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        double currentStamina = getStamina(uuid);

        setStamina(uuid, currentStamina - 0.1);

        setRegenTimeStamina(uuid, 30);
    }

    public void startStamina() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UUID uuid = p.getUniqueId();
                    double currentStamina = getStamina(uuid);
                    int currentRegenTime = getRegenTimeStamina(uuid);
                    boolean isConsumed = false; // 이번 틱에 기력을 썼는지 확인하는 변수
                    Map<String, Tag> tags = getTags(uuid);

                    // 1. 점프 체크 (상태와 상관없이 최우선 순위)
                    int currentJump = p.getStatistic(Statistic.JUMP);
                    if (currentJump > getLastJump(uuid)) {
                        setLastJump(uuid, currentJump);

                        if (tags.containsKey("hot")) {
                            currentStamina -= 0.45;
                        } else {
                            currentStamina -= 0.3;
                        }
                         // 점프 소모량을 체감되게 상향 추천
                        setRegenTimeStamina(uuid, 30); // 2초 대기
                        isConsumed = true;

                    }

                    // 2. 소모 단계 (달리기)
                    if (p.isSprinting()) {

                        if (tags.containsKey("hot")) {
                            currentStamina -= 0.3;
                        } else {
                            currentStamina -= 0.2;
                        }
                        setRegenTimeStamina(uuid, 30);
                        isConsumed = true;

                    }

                    // 3. 회복 단계 (이번 틱에 소모하지 않았을 때만 진입)
                    if (!isConsumed) {
                        if (currentRegenTime > 0) {
                            setRegenTimeStamina(uuid, currentRegenTime - 1);
                        } else if (currentStamina < 100) {
                            currentStamina += 1.0;
                        }
                    }

                    // 4. 최종 데이터 저장 (한 번에 처리)
                    if (currentStamina > 100) currentStamina = 100;
                    if (currentStamina < 0) currentStamina = 0;

                    setStamina(uuid, currentStamina);

                    if (currentStamina <= 0) {
                        addTag(uuid, "exhaust", "탈진", NamedTextColor.YELLOW, 2, 1);
                    }
                }
            }
        }.runTaskTimer(real, 0 ,1);
    }
}
