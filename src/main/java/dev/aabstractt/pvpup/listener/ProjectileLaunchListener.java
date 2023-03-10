package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.hook.CosmeticsHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public final class ProjectileLaunchListener implements Listener {

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent ev) {
        Projectile projectile = ev.getEntity();

        ProjectileSource shooter = projectile.getShooter();
        if (!(shooter instanceof Player)) return;

        CosmeticsHook.handleProjectileEffect((Player) shooter, projectile);
    }
}