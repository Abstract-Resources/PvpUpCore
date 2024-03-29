package dev.aabstractt.pvpup.factory;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ArenaFactory {

    @Getter private final static @NonNull ArenaFactory instance = new ArenaFactory();

    private final @NonNull File file = new File(AbstractPlugin.getInstance().getDataFolder(), "arenas.yml");

    private final Set<Arena> arenas = new HashSet<>();

    public void init() {
        if (!this.file.exists()) return;

        Configuration configuration = YamlConfiguration.loadConfiguration(this.file);

        String defaultWorldName = AbstractPlugin.getDefaultWorldName();
        if (defaultWorldName == null) return;

        World defaultWorld = Bukkit.getWorld(defaultWorldName);
        if (defaultWorld == null) return;

        for (String worldName : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(worldName);

            if (section == null) continue;

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = WorldCreator.name(worldName).createWorld();
                Bukkit.getWorlds().add(world);
            }

            this.registerNewArena(new Arena(
                    worldName,
                    ArenaCuboid.fromSection(world, section.getConfigurationSection("spawn-cuboid")),
                    ArenaCuboid.fromSection(defaultWorld, section.getConfigurationSection("portal-cuboid"))
            ), false);
        }
    }

    public void registerNewArena(@NonNull Arena arena, boolean overwrite) {
        if (!this.isArena(arena.getWorldName())) {
            this.arenas.add(arena);
        }

        arena.recalculate();

        if (!overwrite) return;

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file);

        configuration.set(arena.getWorldName() + ".custom_name", arena.getWorldName());

        ArenaCuboid cuboid = arena.getSpawnCuboid();
        configuration.set(arena.getWorldName() + ".spawn-cuboid", cuboid != null ? cuboid.serialize() : null);

        cuboid = arena.getPortalCuboid();
        configuration.set(arena.getWorldName() + ".portal-cuboid", cuboid != null ? cuboid.serialize() : null);

        try {
            configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @Nullable Arena byWorld(World world) {
        return this.arenas.stream()
                .filter(arena -> arena.getWorldName().equalsIgnoreCase(world.getName()))
                .findAny().orElse(null);
    }

    public @Nullable Arena byName(@Nullable String worldName) {
        if (worldName == null) return null;

        World world = Bukkit.getWorld(worldName);

        return this.byWorld(world);
    }

    public @Nullable Arena byPortal(@NonNull Location location) {
        return this.arenas.stream()
                .filter(arena -> arena.getPortalCuboid() != null && arena.getPortalCuboid().isInCuboid(location))
                .findFirst().orElse(null);
    }

    public boolean isArena(@NonNull String worldName) {
        return this.arenas.stream()
                .anyMatch(arena -> arena.getWorldName().equalsIgnoreCase(worldName));
    }
}