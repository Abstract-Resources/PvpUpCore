package dev.aabstractt.pvpup.object;

import com.google.common.collect.Maps;
import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.datasource.MySQLDataSource;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
    private double coins = 0;
    private int points = 0;

    private int currentPerk = 0;

    private long combatExpireAt = 0;

    private boolean admin = false;

    private @Nullable String currentArenaEditing = null;

    public static void store(@NonNull Profile profile) {
        profilesStored.put(profile.getUniqueId(), profile);
    }

    public void flush() {
        this.currentPerk = 0;
        this.combatExpireAt = 0;
    }

    public static void flush(@NonNull Player player) {
        Profile profile = profilesStored.remove(player.getUniqueId());

        if (profile == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(AbstractPlugin.getInstance(), () -> MySQLDataSource.getInstance().updateProfile(profile));
    }

    public static @Nullable Profile byPlayer(@NonNull Player player) {
        return profilesStored.get(player.getUniqueId());
    }
}
