package dev.aabstractt.pvpup.object;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor @Data
public class Profile {

    private final static Map<UUID, Profile> profilesStored = Maps.newConcurrentMap();

    private final UUID uniqueId;
    private final String name;

    private int currentLevel = 0;
    private int currentPerk = 0;

    public static void store(@NonNull Player player) {
        profilesStored.put(player.getUniqueId(), new Profile(player.getUniqueId(), player.getName()));
    }

    public static void flush(@NonNull Player player) {
        profilesStored.remove(player.getUniqueId());
    }

    public static @Nullable Profile byPlayer(@NonNull Player player) {
        return profilesStored.get(player.getUniqueId());
    }
}
