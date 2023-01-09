package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    public void onPlayerDeathEvent(PlayerDeathEvent ev) {
        Player player = ev.getEntity();

        if (!ArenaFactory.getInstance().isArena(player.getWorld().getName())) return;

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return;

        Perk perk = PerkFactory.getInstance().byLevel(profile.getCurrentLevel());
        profile.setCurrentLevel(perk != null ? perk.getDownLevel() : 0);

        Player killer = player.getKiller();
        if (killer == null) return;

        profile = Profile.byPlayer(killer);
        if (profile == null) return;

        profile.setCurrentLevel(profile.getCurrentLevel() + 1);

        perk = PerkFactory.getInstance().byLevel(profile.getCurrentLevel());
        if (perk == null) return;
        if (profile.getCurrentPerk() == perk.getId()) return;

        profile.setCurrentPerk(perk.getId());

        perk.apply(killer);
    }
}
