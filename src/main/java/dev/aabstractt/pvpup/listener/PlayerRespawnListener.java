package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent ev) {
        Player player = ev.getPlayer();

        if (player == null || !player.isOnline()) return;

        Arena arena = ArenaFactory.getInstance().byWorld(player.getWorld());
        if (arena == null) return;

        World world = Bukkit.getWorld(arena.getWorldName());
        if (world == null) return;

        ev.setRespawnLocation(world.getSpawnLocation());
    }
}