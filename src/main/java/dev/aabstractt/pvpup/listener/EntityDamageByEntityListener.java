package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.TimeUnit;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        Entity entity = ev.getEntity();

        if (!(entity instanceof Player)) return;

        Entity target = ev.getDamager();
        if (!(target instanceof Player)) return;

        Profile profile = Profile.byPlayer((Player) entity);
        if (profile != null) profile.setCombatExpireAt(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));

        Profile targetProfile = Profile.byPlayer((Player) target);
        if (targetProfile != null) targetProfile.setCombatExpireAt(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
    }
}