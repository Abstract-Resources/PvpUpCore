package dev.aabstractt.pvpup.factory;

import com.google.common.collect.ImmutableSet;
import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Perk;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PerkFactory {

    @Getter private final static @NonNull PerkFactory instance = new PerkFactory();

    private final Set<@NonNull Perk> perks = new HashSet<>();

    public void init() {
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(AbstractPlugin.getInstance().getDataFolder(), "perks.yml"));

        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);

            this.perks.add(new Perk(
                    Integer.parseInt(key),
                    section.getInt("slot", -1),
                    section.getInt("page", -1),
                    section.getInt("min-points"),
                    section.getInt("max-points"),
                    section.getInt("down-level"),
                    Perk.deserializeContents(section.getConfigurationSection("contents"))
            ));
        }
    }

    public @Nullable Perk byPoints(int points) {
        return this.perks.stream()
                .filter(perk -> perk.isAccessible(points))
                .findAny().orElse(null);
    }

    public @Nullable Perk byId(int id) {
        return this.perks.stream()
                .filter(perk -> perk.getId() == id)
                .findFirst().orElse(null);
    }

    public @NonNull Set<Perk> all() {
        return ImmutableSet.copyOf(this.perks);
    }
}