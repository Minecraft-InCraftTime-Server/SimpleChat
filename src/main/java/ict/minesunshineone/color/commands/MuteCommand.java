package ict.minesunshineone.color.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ict.minesunshineone.color.managers.MuteManager;
import ict.minesunshineone.color.utils.TimeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class MuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mute")) {
            if (args.length < 3) {
                sender.sendMessage(Component.text("用法: /mute <玩家> <时长(分钟)> <原因>")
                        .color(TextColor.color(255, 170, 0)));  // 深金色
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("找不到该玩家")
                        .color(TextColor.color(255, 170, 0)));  // 深金色
                return true;
            }

            try {
                long duration = TimeUtils.parseDuration(args[1]);
                StringBuilder reason = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    reason.append(args[i]).append(" ");
                }

                muteManager.mutePlayer(target, duration, reason.toString().trim());

                // 使用格式化后的时间显示
                String formattedDuration = TimeUtils.formatDuration(duration);

                Component message = Component.text("你已被管理员禁言")
                        .color(TextColor.color(255, 170, 0))
                        .append(Component.newline())
                        .append(Component.text("原因: ").color(TextColor.color(255, 170, 0)))
                        .append(Component.text(reason.toString().trim()).color(TextColor.color(255, 85, 85)))
                        .append(Component.newline())
                        .append(Component.text("时长: ").color(TextColor.color(255, 170, 0)))
                        .append(Component.text(formattedDuration).color(TextColor.color(255, 85, 85)));

                target.sendMessage(message);
                sender.sendMessage(Component.text("已禁言玩家 ")
                        .color(TextColor.color(255, 170, 0)) // 深金色
                        .append(Component.text(target.getName())
                                .color(TextColor.color(255, 85, 85))));  // 红色

                Component broadcast = Component.text(sender.getName())
                        .color(TextColor.color(255, 85, 85)) // 红色
                        .append(Component.text("禁言了 ").color(TextColor.color(255, 170, 0))) // 深金色
                        .append(Component.text(target.getName()).color(TextColor.color(255, 85, 85))) // 红色
                        .append(Component.text("，禁言时长：").color(TextColor.color(255, 170, 0))) // 深金色
                        .append(Component.text(formattedDuration).color(TextColor.color(255, 85, 85))) // 红色
                        .append(Component.text("。理由：").color(TextColor.color(255, 170, 0))) // 深金色
                        .append(Component.text(reason.toString().trim()).color(TextColor.color(255, 85, 85)));  // 红色

                Bukkit.broadcast(broadcast);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(TimeUtils.getTimeFormatHelpMessage());
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("unmute")) {
            if (args.length < 1) {
                sender.sendMessage(Component.text("用法: /unmute <玩家>")
                        .color(TextColor.color(255, 170, 0)));  // 深金色
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("找不到该玩家")
                        .color(TextColor.color(255, 170, 0)));  // 深金色
                return true;
            }

            muteManager.unmutePlayer(target);
            target.sendMessage(Component.text("你的禁言已被解除！")
                    .color(TextColor.color(255, 170, 0)));  // 深金色);
            sender.sendMessage(Component.text("已解除玩家 ")
                    .color(TextColor.color(255, 170, 0)) // 深金色
                    .append(Component.text(target.getName())
                            .color(TextColor.color(255, 85, 85))));  // 红色

            Component unmuteBroadcast = Component.text(sender.getName())
                    .color(TextColor.color(255, 85, 85)) // 红色
                    .append(Component.text("解除了 ").color(TextColor.color(255, 170, 0))) // 深金色
                    .append(Component.text(target.getName()).color(TextColor.color(255, 85, 85))) // 红色
                    .append(Component.text(" 的禁言").color(TextColor.color(255, 170, 0)));  // 深金色

            Bukkit.broadcast(unmuteBroadcast);
            return true;
        }
        return false;
    }
}
