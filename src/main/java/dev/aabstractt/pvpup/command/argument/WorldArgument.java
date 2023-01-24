package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.PlayerArgument;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class WorldArgument extends PlayerArgument {

    public WorldArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " world <world_name>");

            return;
        }

        World world = Bukkit.getWorld(args[0]);

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World " + args[0] + " not found");

            return;
        }

        sender.teleport(world.getSpawnLocation());
    }
}