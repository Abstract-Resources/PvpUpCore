package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.layout.ScoreboardLayout;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();

        String worldName = AbstractPlugin.getDefaultWorldName();
        if (worldName == null) {
            player.kickPlayer("An error occurred");

            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.kickPlayer("An error occurred");

            return;
        }

        player.teleport(world.getSpawnLocation());

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.getActivePotionEffects().clear();

        ScoreboardLayout.getInstance().store(player);
    }
}
