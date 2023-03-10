package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.PlayerArgument;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class AdminArgument extends PlayerArgument {

    public AdminArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <world>");

            return;
        }

        Arena arena = ArenaFactory.getInstance().byName(args[0]);
        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "This world not is a arena!");

            return;
        }

        Profile profile = Profile.byPlayer(sender);
        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "An error occurred");

            return;
        }

        boolean admin = profile.isAdmin();
        profile.setAdmin(!admin);

        profile.setCurrentArenaEditing(!admin ? arena.getWorldName() : null);

        if (!admin) {
            sender.sendMessage(ChatColor.GREEN + "Admin mode successfully enabled!");

            return;
        }

        ArenaFactory.getInstance().registerNewArena(arena, true);

        sender.sendMessage(ChatColor.RED + "Admin mode successfully disabled!");
    }
}