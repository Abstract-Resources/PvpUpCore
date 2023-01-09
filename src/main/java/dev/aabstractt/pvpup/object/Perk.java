package dev.aabstractt.pvpup.object;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor @Getter
public class Perk {

    private final int id;
    private final int minLevel;
    private final int maxLevel;
    private final int requiredPoints;
    private final int downLevel;

    private final ItemStack[] contents;

    public boolean isAccessible(int currentLevel) {
        return currentLevel >= this.minLevel && this.maxLevel >= currentLevel;
    }

    public void apply(@NonNull Player player) {

    }

    public static ItemStack[] deserializeContents(@NonNull ConfigurationSection configurationSection) {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection section = configurationSection.getConfigurationSection(key);

            if (section == null) continue;

            itemStacks.add(new ItemStack(
                    Material.valueOf(section.getString("material")),
                    section.getInt("amount"),
                    (short) section.getInt("damage"))
            );
        }

        return itemStacks.toArray(new ItemStack[0]);
    }
}