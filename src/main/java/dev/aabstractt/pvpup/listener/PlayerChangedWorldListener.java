package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent ev) {
        Player player = ev.getPlayer();

        if (player == null || !player.isOnline()) return;

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return;

        profile.flush(); // Flush all profile data if the player change of world to prevent issues with that

        if (!ArenaFactory.getInstance().isArena(player.getWorld().getName())) return;

        Perk perk = PerkFactory.getInstance().byId(profile.getCurrentPerk());
        if (perk == null) return;

        perk.apply(player);
    }
}