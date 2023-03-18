package dev.aabstractt.pvpup.command;

import dev.aabstractt.pvpup.command.argument.*;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PvpUpCommand implements CommandExecutor {

    private final Set<Argument> arguments = new HashSet<>();

    public PvpUpCommand() {
        this.registerArgument(
                new CreateArenaArgument("create", "pvpup.admin"),
                new WorldArgument("world", "pvpup.admin"),
                new ForceLevelArgument("forcelevel", "pvpup.admin"),
                new DeletePortalArgument("deleteportal", "pvpup.admin"),
                new SetMinArgument("setmin", "pvpup.admin"),
                new SetMaxArgument("setmax", "pvpup.admin"),
                new SaveArgument("save", "pvpup.admin"),
                new PerksArgument("perks", null)
        );
    }

    private void registerArgument(@NonNull Argument... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
    }

    private @Nullable Argument getArgument(String argumentLabel) {
        return this.arguments.stream()
                .filter(argument -> argument.getName().equalsIgnoreCase(argumentLabel))
                .findAny().orElse(null);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Use /" + s + " help");

            return false;
        }

        Argument argument = this.getArgument(args[0]);

        if (argument == null) {
            commandSender.sendMessage(ChatColor.RED + "Use /" + s + " help");

            return false;
        }

        if (argument.getPermission() != null && !commandSender.hasPermission(argument.getPermission())) {
            commandSender.sendMessage(ChatColor.RED + "You don't have permissions to use this command");

            return false;
        }

        argument.onConsoleExecute(commandSender, s, Arrays.copyOfRange(args, 1, args.length));

        return false;
    }
}
