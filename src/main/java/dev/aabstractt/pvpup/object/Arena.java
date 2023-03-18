package dev.aabstractt.pvpup.object;

import lombok.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

@RequiredArgsConstructor @AllArgsConstructor @Getter
public class Arena {

    private final @NonNull String worldName;

    @Setter private @Nullable ArenaCuboid spawnCuboid = null;
    @Setter private @Nullable ArenaCuboid portalCuboid = null;

    public void recalculate() {
        if (this.spawnCuboid != null) {
            this.spawnCuboid.recalculate();
        }

        if (this.portalCuboid != null) {
            this.portalCuboid.recalculate();
        }
    }

    public static void handlePortalCuboid(boolean first, Arena arena, @NonNull Location location) {
        ArenaCuboid cuboid = arena.getPortalCuboid();
        if (cuboid == null) cuboid = new ArenaCuboid(location.getWorld(), 0, 0, 0, 0, 0, 0);

        if (first) {
            cuboid.setFirstCorner(location);
        } else {
            cuboid.setSecondCorner(location);
        }

        arena.setPortalCuboid(cuboid);
    }

    public static void handleSpawnCuboid(boolean first, Arena arena, @NonNull Location location) {
        ArenaCuboid cuboid = arena.getSpawnCuboid();
        if (cuboid == null) cuboid = new ArenaCuboid(location.getWorld(), 0, 0, 0, 0, 0, 0);

        if (first) {
            cuboid.setFirstCorner(location);
        } else {
            cuboid.setSecondCorner(location);
        }

        arena.setSpawnCuboid(cuboid);
    }
}