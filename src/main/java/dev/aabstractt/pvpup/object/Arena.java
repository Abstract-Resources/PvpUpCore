package dev.aabstractt.pvpup.object;

import lombok.*;

import javax.annotation.Nullable;

@RequiredArgsConstructor @AllArgsConstructor @Getter
public class Arena {

    private final @NonNull String worldName;

    @Setter private @Nullable ArenaCuboid spawnCuboid = null;
    @Setter private @Nullable ArenaCuboid portalCuboid = null;
}