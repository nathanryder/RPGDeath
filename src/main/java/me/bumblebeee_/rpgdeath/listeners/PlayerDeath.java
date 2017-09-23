package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.RPGDeath;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class PlayerDeath implements Listener {

    Random random = new Random();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player t = e.getEntity();

        List<String> messages = RPGDeath.getInstance().getConfig().getStringList("deathMessages");
        if (messages.size() <= 0)
            return;

        Player attacker = t.getKiller();

        String defaultType = RPGDeath.getInstance().getConfig().getString("deathTypes.DEFAULT");
        World w = t.getWorld();
        String attackerName = (attacker == null) ? defaultType : attacker.getDisplayName();
        if (attackerName.equals(defaultType)) {
            for (String rawType : RPGDeath.getInstance().getConfig().getStringList("deathTypes")) {
                EntityDamageEvent.DamageCause type = EntityDamageEvent.DamageCause.valueOf(rawType);
                if (type == null) {
                    RPGDeath.getInstance().getLogger().warning("Failed to match death type of " + rawType);
                    continue;
                }

                if (t.getLastDamageCause().getCause() == type) {
                    attackerName = RPGDeath.getInstance().getConfig().getString("deathTypes." + rawType);
                }
            }
        }

        String message = messages.get(random.nextInt(messages.size()-1));
        message = message.replace("%target_name%", t.getDisplayName());
        message = message.replace("%killer_name%", attackerName);
        message = message.replace("%world%", w.getName());

        e.setDeathMessage(message);
    }
}