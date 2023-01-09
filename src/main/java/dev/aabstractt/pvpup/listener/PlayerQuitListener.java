package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent ev) {
        Profile.flush(ev.getPlayer());
    }
}
