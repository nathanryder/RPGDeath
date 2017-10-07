package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Messages;
import me.bumblebeee_.rpgdeath.util.Runnables;
import me.bumblebeee_.rpgdeath.util.Storage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    Storage storage = new Storage();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getItem() == null)
            return;
        if (e.getItem().getType() == Material.COMPASS) {
            if (!e.getItem().hasItemMeta())
                return;
            if (!e.getItem().getItemMeta().hasDisplayName())
                return;

            ItemStack i = e.getItem();
            String display = RPGDeath.getInstance().getConfig().getString("compass.display").replace("%player%", "");

            if (!i.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', display)))
                return;
            if (!p.hasPermission("death.compass.click"))
                return;
            if (!Runnables.getCoords().containsKey(p.getUniqueId()))
                return;

            //world:x:y:z:player
            String[] data = Runnables.getCoords().get(p.getUniqueId()).split(":");
            if (data.length != 5)
                return;

            for (String line : Messages.CHAT_TRACKER_MESSAGE.get().split(";")) {
                line = line.replace("%world%", data[0]);
                line = line.replace("%x%", data[1]);
                line = line.replace("%y%", data[2]);
                line = line.replace("%z%", data[3]);
                line = line.replace("%player%", data[4]);
                p.sendMessage(line);
            }
        } else if (!storage.getItemType(e.getItem()).equalsIgnoreCase("failed")) {
            if (p.hasPermission("death.death"))
                e.setCancelled(true);
        }
    }
}