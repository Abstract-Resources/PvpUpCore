package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public final class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        Entity entity = ev.getEntity();

        Arena arena = ArenaFactory.getInstance().byWorld(entity.getWorld());
        if (arena == null) return;

        ArenaCuboid cuboid = arena.getSpawnCuboid();
        if (cuboid == null) return;

        Entity target = ev instanceof EntityDamageByEntityEvent ? ((EntityDamageByEntityEvent) ev).getDamager() : null;

        if (!cuboid.isInCuboid(entity.getLocation()) && (target == null || !cuboid.isInCuboid(target.getLocation()))) {
            return;
        }

        ev.setCancelled(true);
    }
}