package dev.aabstractt.pvpup.factory;

import dev.aabstractt.pvpup.object.Arena;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArenaFactory {

    @Getter private final static @NonNull ArenaFactory instance = new ArenaFactory();

    private final Set<Arena> arenas = new HashSet<>();

    public void init() {}

    public void registerNewArena(@NonNull Arena arena) {

    }

    public @Nullable Arena byWorld(World world) {
        return this.arenas.stream()
                .filter(arena -> arena.getWorldName().equalsIgnoreCase(world.getName()))
                .findAny().orElse(null);
    }

    public boolean isArena(@NonNull String worldName) {
        return this.arenas.stream()
                .anyMatch(arena -> arena.getWorldName().equalsIgnoreCase(worldName));
    }
}