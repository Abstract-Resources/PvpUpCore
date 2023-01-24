package dev.aabstractt.pvpup.listener;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();

        ItemStack itemStack = ev.getItem();
        if (itemStack == null) return;
        if (itemStack.getType() == null) return;
        if (itemStack.getType() == Material.AIR) return;

        Block block = ev.getClickedBlock();
        if (block == null) return;

        if (ev.getAction() != Action.RIGHT_CLICK_BLOCK && ev.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Profile profile = Profile.byPlayer(player);
        if (profile == null || !profile.isAdmin()) return;

        Arena arena = ArenaFactory.getInstance().byWorld(player.getWorld());
        if (arena == null) return;

        if (itemStack.getType() == Material.WOOD_AXE) {
            this.handleSpawnCuboid(player, arena, block.getLocation(), ev.getAction() == Action.LEFT_CLICK_BLOCK);
        }
    }

    private void handleSpawnCuboid(@NonNull Player player, @NonNull Arena arena, @NonNull Location location, boolean first) {
        ArenaCuboid cuboid = arena.getSpawnCuboid();
        if (cuboid == null) cuboid = new ArenaCuboid(player.getWorld(), 0, 0, 0, 0, 0, 0);

        if (first) {
            cuboid.setFirstCorner(location);
        } else {
            cuboid.setSecondCorner(location);
        }

        arena.setSpawnCuboid(cuboid);

        player.sendMessage(ChatColor.GREEN + (first ? "First" : "Second") + " spawn corner was successfully changed!");
    }
}