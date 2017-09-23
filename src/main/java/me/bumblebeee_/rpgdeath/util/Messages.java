package me.bumblebeee_.rpgdeath.util;

import me.bumblebeee_.rpgdeath.RPGDeath;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Messages {

    INVALID_ARGS("invalidArguments"),
    NO_PERMISSIONS("noPermissions"),
    PLAYER_OFFLINE("playerOffline"),
    RESPAWN_MSG_ONE("respawnMessageOne"),
    RESPAWN_MSG_TWO("respawnMessageTwo"),
    NOT_DEAD("notDead"),
    NO_PORTAL_PERM("portalPermissionDenied"),
    ALREADY_REVIVING("alreadyReviving"),
    RESURECT_FAILED("resurrectFailed"),
    GIVEN_COMPASS("givenCompass"),
    NO_PLAYERS_FOUND("noPlayersFound"),
    CANNOT_REVIVE_SELF("cannotReviveSelf"),
    PLAYER_IN_COOLDOWN("playerInCooldown"),
    CHAT_TRACKER_MESSAGE("chatLocation"),
    RELOAD_SUCCESS("reloadSuccess");

    String key;
    Messages(String key) {
        this.key = key;
    }

    public String get() {
        YamlConfiguration c = getYaml();
        if (c.getString(key) == null)
            return ChatColor.translateAlternateColorCodes('&', "&cFailed to find a message " + key + "! Please report this to an administrator");
        return ChatColor.translateAlternateColorCodes('&', c.getString(key));
    }

    public static YamlConfiguration getYaml() {
        File f = new File(RPGDeath.getInstance().getDataFolder() + File.separator + "messages.yml");
        YamlConfiguration c = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            c = YamlConfiguration.loadConfiguration(f);
            c.set("invalidArguments", "&cInvalid arguments! Correct usage: %usage%");
            c.set("noPermissions", "&cYou do not have the required permissions!");
            c.set("reloadSuccess", "&aSuccessfully reloaded config!");
            c.set("playerOffline", "&cFailed to find a player called %player%");
            c.set("respawnMessageOne", "&3Caronte &6>> &3Siete deceduto %player%, ora resta solo la vostra anima. Vi ho portato nell'Ade.");
            c.set("respawnMessageTwo", "&3Caronte &6>> &3Fate &d/menumorte &3per accedere al menu dei morti che camminano.");
            c.set("notDead", "&c%player% is not dead!");
            c.set("portalPermissionDenied", "&d&lFATO &4&l>> &dNon potete attraversare il portale dei morti, solo le gilde magiche o mistiche possono.");
            c.set("alreadyReviving", "&c%player% is already being revived!");
            c.set("resurrectFailed", "&cThe resurrection rite has failed!");
            c.set("givenCompass", "&cYou have been given a compass that tracks %player%");
            c.set("noPlayersFound", "&cFailed to find any players to track!");
            c.set("cannotReviveSelf", "&cYou cannot revive yourself!");
            c.set("playerInCooldown", "&c%player% is in cooldown for %time% more minutes");
            c.set("chatLocation", "&6&lInfo for %player% ;&6World: %world% ;&6X: %x% ;&6Y: %y% ;&6Z: %z%");

            try {
                c.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return c == null ? YamlConfiguration.loadConfiguration(f) : c;
    }
}
