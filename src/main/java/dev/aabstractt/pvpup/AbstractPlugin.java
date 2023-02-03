package dev.aabstractt.pvpup;

import dev.aabstractt.pvpup.command.PvpUpCommand;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.layout.ScoreboardLayout;
import dev.aabstractt.pvpup.listener.*;
import dev.aabstractt.pvpup.utils.visualise.WallBorderListener;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class AbstractPlugin extends JavaPlugin {

    @Getter private static AbstractPlugin instance;

    private final static Map<String, String> messages = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        PerkFactory.getInstance().init();
        ArenaFactory.getInstance().init();

        ScoreboardLayout.getInstance().init(
                this.getConfig().getString("scoreboard.title"),
                this.getConfig().getConfigurationSection("scoreboard.lines")
        );

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerChangedWorldListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.getServer().getPluginManager().registerEvents(new WallBorderListener(), this);

        this.getCommand("pvpup").setExecutor(new PvpUpCommand());

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ScoreboardLayout.getInstance().update(player);
                }
            }
        }.runTaskTimerAsynchronously(this, 20, 5);

        ConfigurationSection section = this.getConfig().getConfigurationSection("messages");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            messages.put(key, section.getString(key));
        }
    }

    public static void broadcastActionBar(@NonNull World world, @NonNull String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);

        for (Player player : world.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
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