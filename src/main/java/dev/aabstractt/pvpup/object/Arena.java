package dev.aabstractt.pvpup.object;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import javax.annotation.Nullable;

@RequiredArgsConstructor @Getter
public class Arena {

    private final @NonNull String worldName;

    private @Nullable ArenaCuboid spawnCuboid = null;
}