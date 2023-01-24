package dev.aabstractt.pvpup.factory;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
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
                    ArenaCuboid.fromSection(world, section.getConfigurationSection("portal-cuboid"))
            ), false);
        }
    }

    public void registerNewArena(@NonNull Arena arena, boolean overwrite) {
        this.arenas.add(arena);

        if (!overwrite) return;

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file);

        configuration.set(arena.getWorldName() + ".custom_name", arena.getWorldName());

        ArenaCuboid cuboid = arena.getSpawnCuboid();
        if (cuboid != null) configuration.set(arena.getWorldName() + ".spawn-cuboid", cuboid.serialize());

        cuboid = arena.getPortalCuboid();
        if (cuboid != null) configuration.set(arena.getWorldName() + ".portal-cuboid", cuboid.serialize());

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

    public boolean isArena(@NonNull String worldName) {
        return this.arenas.stream()
                .anyMatch(arena -> arena.getWorldName().equalsIgnoreCase(worldName));
    }
}