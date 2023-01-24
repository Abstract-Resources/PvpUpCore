package dev.aabstractt.pvpup.utils.visualise;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum VisualType {

    //TODO: Figure out a better way for filling blocks than this

    /**
     * Represents the wall approaching claims when Spawn Tagged.
     */
    SPAWN_BORDER() {
        private final BlockFiller blockFiller = new BlockFiller() {
            @Override
            VisualBlockData generate(Player player, Location location) {
                return new VisualBlockData(Material.STAINED_GLASS, DyeColor.RED.getData());
            }
        };

        @Override
        public BlockFiller blockFiller() {
            return blockFiller;
        }
    };

    abstract BlockFiller blockFiller();
}
