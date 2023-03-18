package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.Argument;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public final class DeletePortalArgument extends Argument {

    public DeletePortalArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onConsoleExecute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " admin <world>");

            return;
        }

        Arena arena = ArenaFactory.getInstance().byName(args[0]);
        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "This world not is a arena!");

            return;
        }

        arena.setPortalCuboid(null);
        ArenaFactory.getInstance().registerNewArena(arena, true);

        sender.sendMessage(ChatColor.GREEN + "Portal successfully removed!");
    }
}