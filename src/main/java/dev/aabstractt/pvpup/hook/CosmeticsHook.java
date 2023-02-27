package dev.aabstractt.pvpup.hook;

import dev.aabstractt.cosmetics.factory.CosmeticFactory;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public final class CosmeticsHook {

    public static void handleDeathSound(@NonNull Player player) {
        if (!Bukkit.getPluginManager().isPluginEnabled("AbstractCosmetics")) return;

        CosmeticFactory.getInstance().handleDeathSound(player);
    }

    public static void handleDeathHologram(@NonNull Player player, boolean withoutKiller, @NonNull String... args) {
        if (!Bukkit.getPluginManager().isPluginEnabled("AbstractCosmetics")) return;

        CosmeticFactory.getInstance().handleDeathHologram(player, withoutKiller, args);
    }

    public static void handleProjectileEffect(@NonNull Player player, Projectile projectile) {
        if (!Bukkit.getPluginManager().isPluginEnabled("AbstractCosmetics")) return;

        CosmeticFactory.getInstance().handleProjectileEffect(player, projectile);
    }
}