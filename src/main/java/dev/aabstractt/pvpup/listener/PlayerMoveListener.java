package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent ev) {
        Player player = ev.getPlayer();

        Location to = ev.getTo();
        if (!ArenaFactory.getInstance().isArena(to.getWorld().getName())) return;

        int toX = to.getBlockX();
        int toY = to.getBlockY();
        int toZ = to.getBlockZ();

        Location from = ev.getFrom();
        if (from.getBlockX() == toX && from.getBlockY() == toY && from.getBlockZ() == toZ) {
            return;
        }

        Block block = to.getWorld().getBlockAt(to);
        if (block == null || block.isEmpty()) return;
        if (block.getType() != Material.STATIONARY_WATER) return;

        Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(player, Arrays.asList(player.getInventory().getContents()), player.getTotalExperience(), ""));
    }
}