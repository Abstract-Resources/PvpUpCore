package dev.aabstractt.pvpup.object;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor @Getter
public class Perk {

    private final int id;
    private final int minLevel;
    private final int maxLevel;
    private final int requiredPoints;
    private final int downLevel;

    private final Map<Integer, ItemStack> contents;

    public boolean isAccessible(int currentLevel) {
        return currentLevel >= this.minLevel && this.maxLevel >= currentLevel;
    }

    public void apply(@NonNull Player player) {
        this.contents.forEach((slot, itemStack) -> player.getInventory().setItem(slot, itemStack));
    }

    public static Map<Integer, ItemStack> deserializeContents(@Nullable ConfigurationSection configurationSection) {
        Map<Integer, ItemStack> contents = new HashMap<>();

        if (configurationSection == null) return contents;

        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection section = configurationSection.getConfigurationSection(key);

            if (section == null) continue;

            contents.put(section.getInt("slot"), new ItemStack(
                    Material.valueOf(section.getString("material")),
                    section.getInt("amount"),
                    (short) section.getInt("damage"))
            );
        }

        return contents;
    }
}