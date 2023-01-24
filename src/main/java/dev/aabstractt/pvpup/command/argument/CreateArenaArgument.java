package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.PlayerArgument;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class CreateArenaArgument extends PlayerArgument {

    public CreateArenaArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args) {
        World world = sender.getWorld();

        if (ArenaFactory.getInstance().isArena(world.getName())) {
            sender.sendMessage(ChatColor.RED + "This arena already exists!");

            return;
        }

        ArenaFactory.getInstance().registerNewArena(new Arena(
                world.getName()
        ), true);
    }
}
