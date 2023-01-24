package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent ev) {
        Player player = ev.getEntity();

        Arena arena = ArenaFactory.getInstance().byWorld(player.getWorld());
        if (arena == null) return;

        player.teleport(player.getWorld().getSpawnLocation());
        player.getInventory().clear();
        player.getActivePotionEffects().clear();

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return;

        profile.setDeaths(profile.getDeaths() + 1);

        Perk perk = PerkFactory.getInstance().byLevel(profile.getCurrentLevel());
        profile.setCurrentLevel(perk != null ? perk.getDownLevel() : 0);

        perk = PerkFactory.getInstance().byLevel(profile.getCurrentLevel());
        if (perk != null) perk.apply(player);

        Player killer = player.getKiller();
        if (killer == null) return;

        profile = Profile.byPlayer(killer);
        if (profile == null) return;

        profile.setKills(profile.getKills() + 1);

        profile.setCurrentLevel(profile.getCurrentLevel() + 1);

        perk = PerkFactory.getInstance().byLevel(profile.getCurrentLevel());
        if (perk == null) return;
        if (profile.getCurrentPerk() == perk.getId()) return;

        profile.setCurrentPerk(perk.getId());

        perk.apply(killer);
    }
}
