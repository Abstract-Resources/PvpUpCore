package dev.aabstractt.pvpup.utils;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PacketPlayOutSetSlot;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NmsUtils {

    public static void resendHeldItemPacket(Player player) {
        sendItemPacketAtHeldSlot(player, NmsUtils.getCleanHeldItem(player));
    }

    public static void sendItemPacketAtHeldSlot(Player player, ItemStack stack) {
        sendItemPacketAtSlot(player, stack, player.getInventory().getHeldItemSlot());
    }

    public static void sendItemPacketAtSlot(Player player, ItemStack stack, int index) {
        sendItemPacketAtSlot(player, stack, index, ((CraftPlayer) player).getHandle().defaultContainer.windowId);
    }

    public static void sendItemPacketAtSlot(Player player, net.minecraft.server.ItemStack stack, int index, int windowID) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        if (entityPlayer.playerConnection == null) return;

        // Safeguarding
        if (index < net.minecraft.server.PlayerInventory.getHotbarSize()) {
            index += 36;
        } else if (index > 35) {
            index = 8 - (index - 36);
        }

        entityPlayer.playerConnection.sendPacket(new PacketPlayOutSetSlot(windowID, index, stack));
    }

    public static net.minecraft.server.ItemStack getCleanItem(Inventory inventory, int slot) {
        return ((CraftInventory) inventory).getInventory().getItem(slot);
    }

    public static net.minecraft.server.ItemStack getCleanItem(Player player, int slot) {
        return getCleanItem(player.getInventory(), slot);
    }

    public static net.minecraft.server.ItemStack getCleanHeldItem(Player player) {
        return getCleanItem(player, player.getInventory().getHeldItemSlot());
    }
}