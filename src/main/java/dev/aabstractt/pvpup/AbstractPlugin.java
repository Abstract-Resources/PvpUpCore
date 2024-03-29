package dev.aabstractt.pvpup;

import com.cryptomorin.xseries.messages.ActionBar;
import com.google.common.collect.Maps;
import dev.aabstractt.pvpup.command.PvpUpCommand;
import dev.aabstractt.pvpup.datasource.MySQLDataSource;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.hook.VaultHook;
import dev.aabstractt.pvpup.layout.ScoreboardLayout;
import dev.aabstractt.pvpup.listener.*;
import dev.aabstractt.pvpup.utils.visualise.WallBorderListener;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AbstractPlugin extends JavaPlugin {

    @Getter private static AbstractPlugin instance;

    private final static Map<String, String> messages = new HashMap<>();

    private final static Map<UUID, Long> pendingToClearActionBar = Maps.newConcurrentMap();

    @Override
    public void onEnable() {
        instance = this;

        if (!VaultHook.hook()) {
            this.getLogger().warning("Vault hook not was loaded... All economy commands was disabled.");
        } else {
            this.getLogger().info("Vault hook successfully registered! Now using 'PvpUp Economy' as provider.");
        }

        this.saveDefaultConfig();

        File file = new File(this.getDataFolder(), "hikari.properties");
        if (!file.exists()) this.saveResource("hikari.properties", false);

        MySQLDataSource.getInstance().init(file);

        PerkFactory.getInstance().init();
        ArenaFactory.getInstance().init();

        ScoreboardLayout.getInstance().init(
                this.getConfig().getString("scoreboard.title"),
                this.getConfig().getConfigurationSection("scoreboard.lines")
        );

        this.getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerChangedWorldListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(), this);
        this.getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.getServer().getPluginManager().registerEvents(new WallBorderListener(), this);

        this.getCommand("pvpup").setExecutor(new PvpUpCommand());

        new BukkitRunnable() {

            @Override
            public void run() {
                long now = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    ScoreboardLayout.getInstance().update(player);

                    if (pendingToClearActionBar.getOrDefault(player.getUniqueId(), now) > now) continue;

                    pendingToClearActionBar.remove(player.getUniqueId());

                    ActionBar.clearActionBar(player);
                }
            }
        }.runTaskTimerAsynchronously(this, 20, 5);

        ConfigurationSection section = this.getConfig().getConfigurationSection("messages");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            messages.put(key, section.getString(key));
        }
    }

    public static @Nullable String getDefaultWorldName() {
        return instance.getConfig().getString("default-world");
    }

    public static void broadcastActionBar(@NonNull World world, @NonNull String message) {
        for (Player player : world.getPlayers()) {
            ActionBar.sendActionBar(player, message);

            pendingToClearActionBar.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2));
        }
    }

    public static @NonNull String replacePlaceholders(@NonNull String message, @NonNull String... args) {
        if (messages.containsKey(message)) {
            message = messages.get(message);
        }

        for (int i = 0; i < args.length; i++) {
            message = message.replaceAll("\\{%" + i + "}", args[i]);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}