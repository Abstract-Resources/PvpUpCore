package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.PlayerArgument;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public final class SetMaxArgument extends PlayerArgument {

    public SetMaxArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " setmax <arena> <spawn|portal>");

            return;
        }

        Arena arena = ArenaFactory.getInstance().byName(args[0]);
        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "This world not is a arena!");

            return;
        }

        if (!args[1].equalsIgnoreCase("portal") && !args[1].equalsIgnoreCase("spawn")) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " setmax <arena> <spawn|portal>");

            return;
        }

        if (args[1].equalsIgnoreCase("portal")) {
            Arena.handlePortalCuboid(false, arena, sender.getLocation());
        } else {
            Arena.handleSpawnCuboid(false, arena, sender.getLocation());
        }

        ArenaFactory.getInstance().registerNewArena(arena, true);

        sender.sendMessage(ChatColor.GREEN + "Second " + args[1] + " corner was successfully changed!");
    }
}