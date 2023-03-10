package dev.aabstractt.pvpup.utils.visualise;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WallBorderListener implements Listener {

    private static final int WALL_BORDER_HEIGHT_BELOW_DIFF = 3;
    private static final int WALL_BORDER_HEIGHT_ABOVE_DIFF = 4;
    private static final int WALL_BORDER_HORIZONTAL_DISTANCE = 7;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        int toX = to.getBlockX();
        int toY = to.getBlockY();
        int toZ = to.getBlockZ();

        Location from = event.getFrom();
        if (from.getBlockX() != toX || from.getBlockY() != toY || from.getBlockZ() != toZ) {
            handlePositionChanged(event.getPlayer(), to.getWorld(), toX, toY, toZ);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        onPlayerMove(event); // PlayerTeleportEvent doesn't have a chained handlerList
    }

    private void handlePositionChanged(@NonNull Player player, @NonNull World toWorld, int toX, int toY, int toZ) {
        Arena arena = ArenaFactory.getInstance().byWorld(toWorld);
        if (arena == null) return;

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return;

        ArenaCuboid cuboid = arena.getSpawnCuboid();
        if (cuboid == null) return;
        if (cuboid.isInCuboid(player)) return;
        if (cuboid.isInCuboid(new Location(toWorld, toX, toY, toZ))) return;

        final VisualType visualType = VisualType.SPAWN_BORDER;

        // Clear any visualises that are no longer within distance.
        VisualiseHandler.getInstance().clearVisualBlocks(player, visualType, visualBlock -> {
            Location other = visualBlock.getLocation();
            return other.getWorld().equals(toWorld) && (
                    Math.abs(toX - other.getBlockX()) > WALL_BORDER_HORIZONTAL_DISTANCE ||
                            Math.abs(toY - other.getBlockY()) > WALL_BORDER_HEIGHT_ABOVE_DIFF ||
                            Math.abs(toZ - other.getBlockZ()) > WALL_BORDER_HORIZONTAL_DISTANCE);
        });
        
        if (System.currentTimeMillis() > profile.getCombatExpireAt()) return;

        // Values used to calculate the new visual cuboid height.
        int minHeight = toY - WALL_BORDER_HEIGHT_BELOW_DIFF;
        int maxHeight = toY + WALL_BORDER_HEIGHT_ABOVE_DIFF;
        int minX = toX - WALL_BORDER_HORIZONTAL_DISTANCE;
        int maxX = toX + WALL_BORDER_HORIZONTAL_DISTANCE;
        int minZ = toZ - WALL_BORDER_HORIZONTAL_DISTANCE;
        int maxZ = toZ + WALL_BORDER_HORIZONTAL_DISTANCE;

        boolean inside = false;
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                if (!cuboid.isInCuboid(new Location(player.getWorld(), x, minHeight, z))) continue;

                inside = true;

                break;
            }
        }

        if (!inside) return;

        for (Location edge : cuboid.edges()) {
            if (Math.abs(edge.getBlockX() - toX) > WALL_BORDER_HORIZONTAL_DISTANCE) continue;
            if (Math.abs(edge.getBlockZ() - toZ) > WALL_BORDER_HORIZONTAL_DISTANCE) continue;

            Location first = edge.clone();
            first.setY(minHeight);

            Location second = edge.clone();
            second.setY(maxHeight);
            VisualiseHandler.getInstance().generate(player, new ArenaCuboid(first, second), visualType, false);
        }
    }
}