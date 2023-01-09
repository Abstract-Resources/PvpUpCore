package dev.aabstractt.pvpup.utils.visualise;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class VisualBlock {

    private final VisualType visualType;
    private final VisualBlockData blockData;
    private final Location location;
}
