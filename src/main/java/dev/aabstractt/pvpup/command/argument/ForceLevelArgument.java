package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.command.Argument;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public final class ForceLevelArgument extends Argument {

    public ForceLevelArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onConsoleExecute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " forcelevel <player> <level>");

            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");

            return;
        }

        Profile profile = Profile.byPlayer(target);
        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");

            return;
        }

        if (!NumberUtils.isNumber(args[1])) {
            sender.sendMessage(ChatColor.RED + "Please provide a valid value");

            return;
        }

        Perk perk = PerkFactory.getInstance().byId(Integer.parseInt(args[1]));
        if (perk == null) {
            sender.sendMessage(ChatColor.RED + "Perk with id " + args[1] + " not found!");

            return;
        }

        profile.setCurrentPerk(perk.getId());
        profile.setPoints(perk.getMinPoints());

        perk.apply(target);

        sender.sendMessage(ChatColor.GREEN + target.getName() + " was upgraded to " + perk.getId());
    }
}