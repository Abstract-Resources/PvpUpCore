package dev.aabstractt.pvpup.layout;

import dev.aabstractt.pvpup.factory.ArenaFactory;
import dev.aabstractt.pvpup.utils.PlaceholderExtension;
import lombok.Getter;
import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.*;

public class ScoreboardLayout {

    @Getter private final static @NonNull ScoreboardLayout instance = new ScoreboardLayout();

    private final Set<UUID> players = new HashSet<>();

    private @Nullable String displayName = null;

    private @Nullable ConfigurationSection configurationSection;

    public void init(@NonNull String displayName, @Nullable ConfigurationSection configurationSection) {
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName.length() > 32 ? displayName.substring(0, 32) : displayName);

        this.configurationSection = configurationSection;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderExtension().register();
    }

    public void store(@NonNull Player player) {
        if (!player.isOnline() || this.displayName == null) return;

        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        String uuidString = player.getUniqueId().toString().substring(0, 16);
        if (scoreboard.getObjective(uuidString) == null) {
            scoreboard.registerNewObjective(uuidString, "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) return;

        objective.setDisplayName(this.displayName);

        player.setScoreboard(scoreboard);

        this.players.add(player.getUniqueId());

        this.update(player);
    }

    public void update(@NonNull Player player) {
        if (this.displayName == null) return;
        if (!this.players.contains(player.getUniqueId())) return;

        if (this.configurationSection == null) return;

        Scoreboard scoreboard = player.getScoreboard();

        try {
            Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            if (objective == null) return;

            List<String> lines = this.configurationSection.getStringList(ArenaFactory.getInstance().byWorld(player.getWorld()) == null ? "lobby" : "match");
            if (lines == null || lines.isEmpty()) return;

            lines.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s.length() > 40 ? s.substring(0, 40) : s)));

            for (int score = 1; score <= lines.size(); score++) {
                String string = lines.get(score - 1);

                if (objective.getScore(string).getScore() != score) {
                    objective.getScore(string).setScore(score);
                }
            }

            for (String string : new ArrayList<>(scoreboard.getEntries())) {
                if (!lines.contains(string)) scoreboard.resetScores(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flush(@NonNull Player player) {
        if (!this.players.contains(player.getUniqueId())) return;

        Scoreboard scoreboard = player.getScoreboard();

        for (Team team : scoreboard.getTeams()) team.unregister();

        this.players.remove(player.getUniqueId());
    }
}