package dev.aabstractt.pvpup.object;

import dev.aabstractt.pvpup.AbstractPlugin;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor @Getter
public class Perk {

    private final int id;
    private final int slot;
    private final int page;
    private final int minPoints;
    private final int maxPoints;
    private final int downLevel;

    private final Map<Integer, ItemStack> contents;

    public boolean isAccessible(int currentPoints) {
        return currentPoints >= this.minPoints && this.maxPoints >= currentPoints;
    }

    public void apply(@NonNull Player player) {
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);

        this.contents.forEach((slot, itemStack) -> player.getInventory().setItem(slot, itemStack));
    }

    public @NonNull List<String> contentsAsString() {
        List<String> strings = new ArrayList<>();

        for (ItemStack itemStack : this.contents.values()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null) continue;

            String displayName = itemMeta.getDisplayName();
            if (displayName == null) displayName = AbstractPlugin.replacePlaceholders(itemStack.getType().name());

            Map<Enchantment, Integer> enchantments = itemMeta.getEnchants();
            if (enchantments.isEmpty()) {
                strings.add(displayName);

                continue;
            }

            List<String> enchants = new ArrayList<>();

            enchantments.forEach(((enchantment, level) -> enchants.add(enchantment.getName() + ". " + level)));
            strings.add(displayName + "(" + String.join(", ", enchants) + ")");
        }

        return strings;
    }

    public static Map<Integer, ItemStack> deserializeContents(@Nullable ConfigurationSection configurationSection) {
        Map<Integer, ItemStack> contents = new HashMap<>();

        if (configurationSection == null) return contents;

        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection section = configurationSection.getConfigurationSection(key);

            if (section == null) continue;

            contents.put(Integer.parseInt(key), new ItemStack(
                    Material.valueOf(section.getString("material")),
                    section.getInt("amount"),
                    (short) section.getInt("damage", 0))
            );
        }

        return contents;
    }
}