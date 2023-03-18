package dev.aabstractt.pvpup.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public final class CreatureSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent ev) {
        Entity entity = ev.getEntity();

        if (entity instanceof Player) return;

        ev.setCancelled(true);
    }
}