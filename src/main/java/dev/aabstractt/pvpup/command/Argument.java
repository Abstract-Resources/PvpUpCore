package dev.aabstractt.pvpup.command;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

@RequiredArgsConstructor @Getter
abstract public class Argument {

    private final @NonNull String name;
    private final @Nullable String permission;

    public abstract void onConsoleExecute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] args);
}