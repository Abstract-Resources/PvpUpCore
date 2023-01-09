package dev.aabstractt.pvpup.factory;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Perk;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class PerkFactory {

    @Getter private final static @NonNull PerkFactory instance = new PerkFactory();

    private final Set<Perk> perks = new HashSet<>();

    public void init() {
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(AbstractPlugin.getInstance().getDataFolder(), "perks.yml"));

        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);

            this.perks.add(new Perk(
                    section.getInt("id"),
                    section.getInt("min-level"),
                    section.getInt("max-level"),
                    section.getInt("required-points"),
                    section.getInt("down-level"),
                    Perk.deserializeContents(section.getConfigurationSection("contents"))
            ));
        }
    }

    public @Nullable Perk byLevel(int currentLevel) {
        return this.perks.stream()
                .filter(perk -> perk.isAccessible(currentLevel))
                .findAny().orElse(null);
    }
}