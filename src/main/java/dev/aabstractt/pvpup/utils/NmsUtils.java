package dev.aabstractt.pvpup.utils;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PacketPlayOutSetSlot;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class NmsUtils {

    public final static String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

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

    /**
     * Returns a minecraft NMS class
     *
     * @param nmsClassString the class name
     * @return the NMS class
     */
    public static Class<?> getNMSClass(String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server" + nmsClassString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets field reflectively
     *
     * @param clazz Class<?>
     * @param fieldName String
     * @param obj Object
     * @return <T> T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Object obj) {
        try {
            return (T) setAccessible(clazz.getDeclaredField(fieldName)).get(obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    /**
     * Gets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T extends AccessibleObject> T getFieldValueNotDeclared(Class<?> clazz, String fieldName, Object obj) {
        try {
            return (T) setAccessible(clazz.getField(fieldName)).get(obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Gets Method reflectively
     *
     * @param clazz
     * @param methodName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getMethodValue(Class<?> clazz, String methodName, Class<?>... obj) {
        try {
            return clazz.getDeclaredMethod(methodName, obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Gets Method reflectively
     *
     * @param clazz
     * @param methodName
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getMethodNotDeclaredValue(Class<?> clazz, String methodName, Class<?>... obj) {
        try {
            return clazz.getMethod(methodName, obj);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Sets accessibleobject accessible state an returns this object
     *
     * @param <T>
     * @param object
     * @return
     */
    public static <T extends AccessibleObject> T setAccessible(T object) {
        object.setAccessible(true);
        return object;
    }

    /**
     * Sets field reflectively
     *
     * @param clazz
     * @param fieldName
     * @param obj
     * @param value
     */
    public static void setFieldValue(Class<?> clazz, String fieldName, Object obj, Object value) {
        try {
            setAccessible(clazz.getDeclaredField(fieldName)).set(obj, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void sendPacket(Player to, Object packet) {
        try {
            Object playerHandle = to.getClass().getMethod("getHandle").invoke(to);

            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);

            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}