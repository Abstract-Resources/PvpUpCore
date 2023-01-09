package dev.aabstractt.pvpup.factory;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Arena;
import dev.aabstractt.pvpup.object.ArenaCuboid;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ArenaFactory {

    @Getter private final static @NonNull ArenaFactory instance = new ArenaFactory();

    private final Set<Arena> arenas = new HashSet<>();

    public void init() {
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(AbstractPlugin.getInstance().getDataFolder(), "arenas.yml"));

        for (String worldName : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(worldName);

            if (section == null) continue;

            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            this.registerNewArena(new Arena(
                    worldName,
                    ArenaCuboid.fromSection(world, section.getConfigurationSection("spawn-cuboid")),
                    ArenaCuboid.fromSection(world, section.getConfigurationSection("portal-cuboid"))
            ));
        }
    }

    public void registerNewArena(@NonNull Arena arena) {
        this.arenas.add(arena);
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