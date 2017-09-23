package me.bumblebeee_.rpgdeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getEntity();
        if (!p.hasPermission("death.invincibility"))
            return;
        e.setCancelled(true);
    }

}