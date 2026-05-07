package org.role.realistic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class Cmd extends Util implements CommandExecutor {

    public Cmd(Realistic real, Values values) {
        super(real, values);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("플레이어만 사용할 수 있는 명령어입니다.", NamedTextColor.RED));
            return true;
        }

        String commandName = command.getName();
        Player p = player.getPlayer();
        UUID uuid = Objects.requireNonNull(player.getPlayer()).getUniqueId();

        switch (commandName) {
            case "thirsty":
                if (args.length < 1) {
                    Objects.requireNonNull(p).sendMessage(Component.text("정수를 입력하여 갈증 수치 조절", NamedTextColor.RED));
                    return true;
                }

                setThirsty(uuid, Integer.parseInt(args[0]));
                return true;
            case "bleed":
                player.sendMessage(Component.text("출혈 상태가 되었습니다!", NamedTextColor.RED));
                addTag(uuid, "bleed", "출혈", NamedTextColor.RED, 100, 1);
                return true;
            default:
                return true;
        }
    }
}
