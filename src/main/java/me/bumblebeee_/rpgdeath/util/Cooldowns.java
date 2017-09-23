package me.bumblebeee_.rpgdeath.util;

import lombok.Getter;
import me.bumblebeee_.rpgdeath.RPGDeath;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldowns {

    private static @Getter Map<UUID, Integer> cooldown = new HashMap<>();

    public void addPlayer(UUID uuid, int time) {
        cooldown.put(uuid, time);
    }

    public void removePlayer(UUID uuid) {
        cooldown.remove(uuid);
    }

    public boolean isPlayerActive(UUID uuid) {
        return cooldown.containsKey(uuid);
    }

    public int getCooldown(UUID uuid) {
        if (!isPlayerActive(uuid))
            return 0;
        return cooldown.get(uuid);
    }

    public void savaData() {
        File f = new File(RPGDeath.getInstance().getDataFolder() + File.separator + "storage.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);

        for (UUID uuid : cooldown.keySet()) {
            c.set("uuids." + uuid, cooldown.get(uuid));
        }

        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        File f = new File(RPGDeath.getInstance().getDataFolder() + File.separator + "storage.yml");
        if (!f.exists())
            return;
        YamlConfiguration c = YamlConfiguration.loadConfiguration(f);

        if (c.getConfigurationSection("uuids") == null)
            return;
        for (String rawUUID : c.getConfigurationSection("uuids").getKeys(false)) {
            UUID uuid = UUID.fromString(rawUUID);
            cooldown.put(uuid, c.getInt("uuids." + rawUUID));
        }

        c.set("uuids", null);
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
