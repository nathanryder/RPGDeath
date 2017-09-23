package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Messages;
import me.bumblebeee_.rpgdeath.util.Runnables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null)
            return;
        if (e.getItem().getType() != Material.COMPASS)
            return;
        if (!e.getItem().hasItemMeta())
            return;
        if (!e.getItem().getItemMeta().hasDisplayName())
            return;

        Player p = e.getPlayer();
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
    }
}