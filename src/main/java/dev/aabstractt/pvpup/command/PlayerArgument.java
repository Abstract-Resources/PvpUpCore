package dev.aabstractt.pvpup.command;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

abstract public class PlayerArgument extends Argument {

    public PlayerArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onConsoleExecute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Run this command in-game");

            return;
        }

        this.onPlayerExecute((Player) sender, commandLabel, args);
    }

    public abstract void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args);
}