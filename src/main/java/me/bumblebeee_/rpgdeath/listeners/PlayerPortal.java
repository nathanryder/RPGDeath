package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.util.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
            return;
        if ((!e.getPlayer().hasPermission("death.nether")) || (!e.getPlayer().hasPermission("death.bypass"))) {
            e.getPlayer().sendMessage(Messages.NO_PORTAL_PERM.get());
            e.setCancelled(true);
        }
    }

}