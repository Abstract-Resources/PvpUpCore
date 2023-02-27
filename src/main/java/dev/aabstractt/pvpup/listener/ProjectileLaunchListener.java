package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.hook.CosmeticsHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public final class ProjectileLaunchListener implements Listener {

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent ev) {
        Projectile projectile = ev.getEntity();

        CosmeticsHook.handleProjectileEffect((Player) projectile.getShooter(), projectile);
    }
}