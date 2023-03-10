package dev.aabstractt.pvpup.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.aabstractt.cosmetics.AbstractPlugin;
import dev.aabstractt.pvpup.object.Profile;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public final class MySQLDataSource {

    @Getter private final static @NonNull MySQLDataSource instance = new MySQLDataSource();

    private @Nullable HikariDataSource dataSource = null;
    private @Nullable HikariConfig hikariConfig = null;

    public void init(@NonNull File file) {
        HikariConfig hikariConfig = new HikariConfig(file.toString());

        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setValidationTimeout(120000);

        this.dataSource = new HikariDataSource(this.hikariConfig = hikariConfig);

        if (this.disconnected()) {
            throw new RuntimeException("An error occurred while attempt load mysql database!");
        }

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS pvpup_profiles (rowId INT PRIMARY KEY AUTO_INCREMENT, uuid TEXT, coins INT, kills INT, deaths INT, play_time INT)")) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public @Nullable Profile loadProfile(@NonNull UUID uuidParsed, @NonNull String name) {
        if (this.disconnected() || this.dataSource == null) return this.reconnect() ? this.loadProfile(uuidParsed, name) : null;

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM pvpup_profiles WHERE uuid = ?")) {
                preparedStatement.setString(1, uuidParsed.toString());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Profile profile = new Profile(uuidParsed, name);

                        profile.setCoins(resultSet.getInt("coins"));
                        profile.setDeaths(resultSet.getInt("deaths"));
                        profile.setKills(resultSet.getInt("kills"));

                        return profile;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createProfile(@NonNull UUID uuidParsed) {
        if (this.disconnected() || this.dataSource == null) {
            if (this.reconnect()) this.createProfile(uuidParsed);

            return;
        }

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pvpup_profiles (uuid, coins, deaths, kills, play_time) VALUES (?, ?, ?, ?, ?)")) {
                preparedStatement.setString(1, uuidParsed.toString());
                preparedStatement.setInt(2, 0);
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 0);
                preparedStatement.setInt(5, 0);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(@NonNull Profile profile) {
        if (this.disconnected() || this.dataSource == null) {
            if (this.reconnect()) this.updateProfile(profile);

            return;
        }

        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE pvpup_profiles SET coins = ?, kills = ?, deaths = ? WHERE uuid = ?")) {
                preparedStatement.setDouble(1, profile.getCoins());
                preparedStatement.setInt(2, profile.getKills());
                preparedStatement.setInt(3, profile.getDeaths());

                preparedStatement.setString(4, profile.getUniqueId().toString());

                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean reconnect() {
        this.close();

        if (this.hikariConfig == null) {
            AbstractPlugin.getInstance().getLogger().severe("Can't reconnect because Hikari config is null...");

            return false;
        }

        try {
            this.dataSource = new HikariDataSource(this.hikariConfig);
        } catch (Exception e) {
            AbstractPlugin.getInstance().getLogger().severe("Can't reconnect because, reason: " + e.getMessage());

            return false;
        }

        return true;
    }

    private boolean disconnected() {
        return this.dataSource == null || this.dataSource.isClosed() || !this.dataSource.isRunning();
    }

    public void close() {
        if (this.dataSource != null) this.dataSource.close();
    }
}