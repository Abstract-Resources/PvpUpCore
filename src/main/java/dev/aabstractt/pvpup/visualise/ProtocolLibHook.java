package dev.aabstractt.pvpup.visualise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import dev.aabstractt.pvpup.AbstractPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Reference http://wiki.vg/Protocol
 */
public final class ProtocolLibHook {

    private static final int STARTED_DIGGING = 0;
    private static final int FINISHED_DIGGING = 2;

    private ProtocolLibHook() {}

    /**
     * Hooks ProtocolLibrary into a {@link JavaPlugin}.
     */
    public static void hook() {
        VisualiseHandler handler = VisualiseHandler.getInstance();

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(AbstractPlugin.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_PLACE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                StructureModifier<Integer> modifier = event.getPacket().getIntegers();
                Player player = event.getPlayer();

                try {
                    int face = modifier.read(3);
                    if (face == 255) {
                        return;
                    }

                    Location clickedBlock = new Location(player.getWorld(), modifier.read(0), modifier.read(1), modifier.read(2));
                    if (handler.getVisualBlockAt(player, clickedBlock) != null) {
                        Location placedLocation = clickedBlock.clone();
                        switch (face) {
                            case 2:
                                placedLocation.add(0, 0, -1);
                                break;
                            case 3:
                                placedLocation.add(0, 0, 1);
                                break;
                            case 4:
                                placedLocation.add(-1, 0, 0);
                                break;
                            case 5:
                                placedLocation.add(1, 0, 0);
                                break;
                            default:
                                return;
                        }

                        if (handler.getVisualBlockAt(player, placedLocation) == null) {
                            event.setCancelled(true);
                            player.sendBlockChange(placedLocation, Material.AIR, (byte) 0);
                            //NmsUtils.resendHeldItemPacket(player);
                        }
                    }
                } catch (FieldAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(AbstractPlugin.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                StructureModifier<Integer> modifier = event.getPacket().getIntegers();

                try {
                    int status = modifier.read(4);
                    if (status == STARTED_DIGGING || status == FINISHED_DIGGING) {
                        Player player = event.getPlayer();
                        int x = modifier.read(0), y = modifier.read(1), z = modifier.read(2);
                        Location location = new Location(player.getWorld(), x, y, z);
                        VisualBlock visualBlock = handler.getVisualBlockAt(player, location);
                        if (visualBlock != null) {
                            event.setCancelled(true);
                            VisualBlockData data = visualBlock.getBlockData();
                            if (status == FINISHED_DIGGING) {
                                player.sendBlockChange(location, data.getBlockType(), data.getData());
                            }/* else if (status == STARTED_DIGGING) {
                                EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                                if (player.getGameMode() == GameMode.CREATIVE || entityPlayer.world.getType(x, y, z).getDamage(entityPlayer, entityPlayer.world, x, y, z) >= 1.0F) {
                                    player.sendBlockChange(location, data.getBlockType(), data.getData());
                                }
                            }*/
                        }
                    }
                } catch (FieldAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
