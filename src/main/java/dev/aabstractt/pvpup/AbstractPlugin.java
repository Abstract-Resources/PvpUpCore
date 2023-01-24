package dev.aabstractt.pvpup;

import dev.aabstractt.pvpup.command.PvpUpCommand;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.layout.ScoreboardLayout;
import dev.aabstractt.pvpup.listener.*;
import dev.aabstractt.pvpup.utils.visualise.WallBorderListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AbstractPlugin extends JavaPlugin {

    @Getter private static AbstractPlugin instance;

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
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
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
    }
}