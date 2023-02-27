package dev.aabstractt.pvpup.object.economy;

import dev.aabstractt.pvpup.AbstractPlugin;
import dev.aabstractt.pvpup.object.Profile;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class PvpUpEconomy implements Economy {

    private static final NumberFormat PRETTY_FORMAT = NumberFormat.getInstance(Locale.US);

    static {
        PRETTY_FORMAT.setRoundingMode(RoundingMode.FLOOR);
        PRETTY_FORMAT.setGroupingUsed(true);
        PRETTY_FORMAT.setMinimumFractionDigits(2);
        PRETTY_FORMAT.setMaximumFractionDigits(2);
    }

    @Override
    public boolean isEnabled() {
        return AbstractPlugin.getInstance().isEnabled();
    }

    @Override
    public String getName() {
        return "PvpUp Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        BigDecimal bigDecimal = BigDecimal.valueOf(v);

        String currency = PRETTY_FORMAT.format(bigDecimal);

        if (toString().endsWith(".00")) currency = currency.substring(0, currency.length() - 3);

        return (bigDecimal.signum() < 0 ? "-" : "") + currency;
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(@NonNull String s) {
        return Bukkit.getOfflinePlayer(s) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return this.hasAccount(offlinePlayer.getName());
    }

    @Override
    public boolean hasAccount(@NonNull String name, String worldName) {
        return this.hasAccount(name);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return this.hasAccount(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) return 0;

        Profile profile = Profile.byPlayer(player);

        return profile != null ? profile.getCoins() : 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return this.getBalance(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String s, String s1) {
        return this.getBalance(s);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return this.getBalance(offlinePlayer.getName());
    }

    @Override
    public boolean has(String s, double v) {
        return this.getBalance(s) >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return this.has(offlinePlayer.getName(), v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return this.has(s, v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return this.has(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) return new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Player is not online");

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Player is not online");

        if (!this.has(s, v)) {
            return new EconomyResponse(v, profile.getCoins(), EconomyResponse.ResponseType.FAILURE, "Player not enough coins");
        }

        profile.setCoins(profile.getCoins() - v);

        return new EconomyResponse(v, profile.getPoints(), EconomyResponse.ResponseType.SUCCESS, "Successfully withdraw");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return this.withdrawPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return this.withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return this.withdrawPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        if (player == null) return new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Player is not online");

        Profile profile = Profile.byPlayer(player);
        if (profile == null) return new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Player is not online");

        profile.setCoins(profile.getCoins() + v);

        return new EconomyResponse(v, profile.getPoints(), EconomyResponse.ResponseType.SUCCESS, "Successfully withdraw");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return this.depositPlayer(offlinePlayer.getName(), v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return this.depositPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return this.depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}