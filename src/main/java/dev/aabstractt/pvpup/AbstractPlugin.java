package dev.aabstractt.pvpup;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.listener.PlayerDeathListener;
import dev.aabstractt.pvpup.listener.PlayerJoinListener;
import dev.aabstractt.pvpup.listener.PlayerQuitListener;
import dev.aabstractt.pvpup.utils.visualise.WallBorderListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class AbstractPlugin extends JavaPlugin {

    @Getter private static AbstractPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        PerkFactory.getInstance().init();
        ArenaFactory.getInstance().init();

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.getServer().getPluginManager().registerEvents(new WallBorderListener(), this);
    }
}