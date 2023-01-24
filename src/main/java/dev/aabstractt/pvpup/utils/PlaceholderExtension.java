package dev.aabstractt.pvpup.utils;

import dev.aabstractt.pvpup.factory.PerkFactory;
import dev.aabstractt.pvpup.object.Perk;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class PlaceholderExtension extends PlaceholderExpansion {

    @Override
    public @NonNull String getIdentifier() {
        return "pvpup";
    }

    @Override
    public @NonNull String getAuthor() {
        return "aabstractt";
    }

    @Override
    public @NonNull String getVersion() {
        return "0.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NonNull String params) {
        if (player == null || !player.isOnline()) return null;

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return null;

        if (params.equals("name")) return player.getName();
        if (params.equals("kills")) return String.valueOf(profile.getKills());
        if (params.equals("deaths")) return String.valueOf(profile.getDeaths());
        if (params.equals("coins")) return String.valueOf(profile.getCoins());
        if (params.equals("current_level")) return String.valueOf(profile.getCurrentPerk());
        if (params.equals("points")) return String.valueOf(profile.getPoints());
        if (params.equals("next_level")) return String.valueOf(profile.getCurrentPerk() + 1);

        if (params.equals("next_level_points")) {
            Perk perk = PerkFactory.getInstance().byId(profile.getCurrentPerk() + 1);
            return perk != null ? String.valueOf(perk.getMinPoints()) : "&cNone";
        }

        return null;
    }
}