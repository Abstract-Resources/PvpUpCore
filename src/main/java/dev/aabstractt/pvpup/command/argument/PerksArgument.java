package dev.aabstractt.pvpup.command.argument;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.command.PlayerArgument;
import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PerksArgument extends PlayerArgument {

    public PerksArgument(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @Override
    public void onPlayerExecute(@NonNull Player sender, @NonNull String commandLabel, @NonNull String[] args) {
        Profile profile = Profile.byPlayer(sender);

        if (profile == null) {
            return;
        }

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .rows(6)
                .title(Component.text(ChatColor.GREEN + "Perks per Level"))
                .disableItemDrop()
                .disableAllInteractions()
                .create();

        this.applyItems(profile, gui, 1);

        gui.open(sender);
    }

    private void applyItems(Profile profile, @NonNull Gui gui, int targetPage) {
        Map<Integer, GuiItem> guiItems = new HashMap<>();
        for (Perk perk : PerkFactory.getInstance().all()) {
            if (perk.getSlot() == -1 || perk.getPage() != targetPage) continue;

            int data = perk.isAccessible(profile.getPoints()) ? 5 : (profile.getCurrentPerk() + 1 == perk.getId() ? 4 : 14);

            guiItems.put(perk.getSlot(), ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) data))
                    .name(Component.text(AbstractPlugin.replacePlaceholders("LEVELS_MENU_ITEM_" + (data == 5 ? "REACHED" : (data == 4 ? "PROGRESS" : "LOCKED")), String.valueOf(perk.getId()))))
                    .lore(Arrays.stream(AbstractPlugin.replacePlaceholders("LEVELS_MENU_ITEM_LORE_" + (data == 5 ? "REACHED" : (data == 4 ? "PROGRESS" : "LOCKED")),
                            String.valueOf(perk.getMinPoints()),
                            String.join("\n", perk.contentsAsString()),
                            String.valueOf(profile.getPoints()),
                            String.valueOf(perk.getMinPoints() - profile.getPoints())
                    ).split("\n")).map(Component::text).collect(Collectors.toList()))
                    .asGuiItem()
            );
        }

        if (guiItems.isEmpty()) return;

        gui.getInventory().clear();
        gui.getGuiItems().clear();

        gui.getGuiItems().putAll(guiItems);

        gui.setItem(51, ItemBuilder.from(Material.PAPER)
                .name(Component.text(AbstractPlugin.replacePlaceholders("NEXT_PAGE_ITEM", String.valueOf(targetPage))))
                .asGuiItem(event -> this.applyItems(profile, gui, targetPage + 1))
        );

        for (int i = 0; i < 9 * 6; i++) {
            if (gui.getGuiItem(i) != null) continue;

            gui.setItem(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).name(Component.text("")).asGuiItem());
        }

        if (targetPage - 1 > 0) {
            gui.setItem(49, ItemBuilder.from(Material.PAPER)
                    .name(Component.text(AbstractPlugin.replacePlaceholders("PREVIOUS_PAGE_ITEM", String.valueOf(targetPage - 1))))
                    .asGuiItem(event -> this.applyItems(profile, gui, targetPage - 1))
            );
        }

        gui.update();
    }
}