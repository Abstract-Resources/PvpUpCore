package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.layout.ScoreboardLayout;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        ScoreboardLayout.getInstance().store(ev.getPlayer());
    }
}
