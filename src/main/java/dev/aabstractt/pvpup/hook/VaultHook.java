package dev.aabstractt.pvpup.hook;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.economy.PvpUpEconomy;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public final class VaultHook {

    private static Economy economy = null;

    public static boolean hook() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;

        economy = new PvpUpEconomy();

        Bukkit.getServicesManager().register(Economy.class, economy, AbstractPlugin.getInstance(), ServicePriority.Highest);

        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        return registeredServiceProvider != null && registeredServiceProvider.getProvider() instanceof PvpUpEconomy;
    }

    public static boolean depositPlayer(@NonNull final Player player, final double amount) {
        return hooked() && economy.depositPlayer(player, amount).transactionSuccess();
    }

    public static boolean decreasePlayerBalance(@NonNull final Player player, final double amount) {
        return hooked() && economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public static boolean hooked() {
        return economy != null;
    }
}