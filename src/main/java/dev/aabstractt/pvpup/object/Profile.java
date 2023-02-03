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

    private int kills = 0;
    private int deaths = 0;
    private int coins = 0;
    private int points = 0;

    private int currentPerk = 0;

    private long combatExpireAt = 0;

    private boolean admin = false;

    public static void store(@NonNull Player player) {
        profilesStored.put(player.getUniqueId(), new Profile(player.getUniqueId(), player.getName()));
    }

    public void flush() {
        this.currentPerk = 0;
        this.combatExpireAt = 0;
        this.admin = false;
    }

    public static void flush(@NonNull Player player) {
        profilesStored.remove(player.getUniqueId());
    }

    public static @Nullable Profile byPlayer(@NonNull Player player) {
        return profilesStored.get(player.getUniqueId());
    }
}
