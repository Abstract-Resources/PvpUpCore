package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.hook.CosmeticsHook;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import dev.aabstractt.pvpup.utils.visualise.VisualType;
import dev.aabstractt.pvpup.utils.visualise.VisualiseHandler;
import org.bukkit.Bukkit;
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

        player.setFallDistance(0);

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.getActivePotionEffects().clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(AbstractPlugin.getInstance(), () -> {
            player.spigot().respawn();

            VisualiseHandler.getInstance().clearVisualBlocks(player, VisualType.SPAWN_BORDER, visualBlock -> true);
        }, 10);

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return;

        profile.setDeaths(profile.getDeaths() + 1);

        Perk perk = PerkFactory.getInstance().byPoints(profile.getPoints());
        profile.setCurrentPerk(perk != null ? perk.getDownLevel() : 0);

        perk = PerkFactory.getInstance().byId(profile.getCurrentPerk());
        if (perk != null) {
            perk.apply(player);

            profile.setPoints(perk.getMinPoints());
        }

        Player killer = player.getKiller();

        CosmeticsHook.handleDeathSound(player);
        CosmeticsHook.handleDeathHologram(player,
                killer == null,
                player.getName(),
                killer != null ? killer.getName() : "none",
                String.valueOf(profile.getCoins()),
                String.valueOf(profile.getDeaths()),
                String.valueOf(profile.getKills())
        );

        player.teleport(player.getWorld().getSpawnLocation());

        if (killer == null) {
            AbstractPlugin.broadcastActionBar(player.getWorld(), AbstractPlugin.replacePlaceholders("PLAYER_DEATH_WITHOUT_KILLER", player.getName()));

            return;
        }

        killer.setHealth(killer.getMaxHealth());

        profile = Profile.byPlayer(killer);
        if (profile == null) return;

        AbstractPlugin.broadcastActionBar(player.getWorld(), AbstractPlugin.replacePlaceholders(
                "PLAYER_DEATH_WITH_KILLER",
                player.getName(),
                killer.getName()
        ));

        profile.setKills(profile.getKills() + 1);
        profile.setPoints(profile.getPoints() + 1);

        perk = PerkFactory.getInstance().byPoints(profile.getPoints());
        if (perk == null) return;
        if (profile.getCurrentPerk() == perk.getId()) return;

        profile.setCurrentPerk(perk.getId());

        perk.apply(killer);
    }
}