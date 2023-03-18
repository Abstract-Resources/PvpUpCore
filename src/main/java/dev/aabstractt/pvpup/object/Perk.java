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

        for (String slot : configurationSection.getKeys(false)) {
            ConfigurationSection mainSection = configurationSection.getConfigurationSection(slot);

            if (mainSection == null) continue;

            ItemStack itemStack = new ItemStack(
                    Material.valueOf(mainSection.getString("material")),
                    mainSection.getInt("amount"),
                    (short) mainSection.getInt("damage", 0)
            );

            List<List<Object>> enchants = (List<List<Object>>) mainSection.getList("enchants");
            if (enchants == null) continue;

            for (List<Object> enchantmentData : enchants) {
                if (enchantmentData == null || enchantmentData.isEmpty()) continue;

                Enchantment enchantment = Enchantment.getByName((String) enchantmentData.get(0));
                if (enchantment == null) continue;

                itemStack.addEnchantment(enchantment, (Integer) enchantmentData.get(1));
            }

            contents.put(Integer.parseInt(slot), itemStack);
        }

        return contents;
    }
}