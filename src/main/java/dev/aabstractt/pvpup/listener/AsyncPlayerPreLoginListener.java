package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.datasource.MySQLDataSource;
import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public final class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler (priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent ev) {
        if (ev.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        Profile profile = MySQLDataSource.getInstance().loadProfile(ev.getUniqueId(), ev.getName());

        if (profile == null) {
            profile = new Profile(ev.getUniqueId(), ev.getName());

            MySQLDataSource.getInstance().createProfile(ev.getUniqueId());
        }

        Profile.store(profile);
    }
}