package dev.aabstractt.pvpup.utils.visualise;

import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.aabstractt.pvpup.utils.packetwrapper.WrapperPlayServerMultiBlockChange;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("deprecation")
public class VisualiseUtil {

    public static void handleBlockChanges(Player player, Map<Location, MaterialData> input) throws IOException {
        if (input.isEmpty()) {
            return;
        }

        if (input.size() == 1) {
            Map.Entry<Location, MaterialData> entry = input.entrySet().iterator().next();
            MaterialData materialData = entry.getValue();
            player.sendBlockChange(entry.getKey(), materialData.getItemType(), materialData.getData());
            return;
        }

        Table<Chunk, Location, MaterialData> table = HashBasedTable.create();
        for (Map.Entry<Location, MaterialData> entry : input.entrySet()) {
            Location location = entry.getKey();
            if (location.getWorld().isChunkLoaded(((int) location.getX()) >> 4, ((int) location.getZ()) >> 4)) {
                table.row(entry.getKey().getChunk()).put(location, entry.getValue());
            }
        }

        for (Map.Entry<Chunk, Map<Location, MaterialData>> entry : table.rowMap().entrySet()) {
            VisualiseUtil.sendBulk(player, entry.getKey(), entry.getValue());
        }
    }

    private static void sendBulk(Player player, Chunk chunk, Map<Location, MaterialData> input) throws IOException {
        MultiBlockChangeInfo[] blockChangeInfo = new MultiBlockChangeInfo[input.size()];
        int i = 0;
        for (Map.Entry<Location, MaterialData> entry : input.entrySet()) {
            MaterialData data = entry.getValue();

            WrappedBlockData wrappedBlockData = WrappedBlockData.createData(data.getItemType());
            wrappedBlockData.setData(data.getData());
            blockChangeInfo[i++] = new MultiBlockChangeInfo(entry.getKey(), wrappedBlockData);
        }
        WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange();
        packet.setChunk(new ChunkCoordIntPair(chunk.getX(), chunk.getZ()));
        packet.setRecords(blockChangeInfo);
        packet.sendPacket(player);
    }
}