package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.util.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClick implements Listener {

    Storage storage = new Storage();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!p.hasPermission("death.death"))
            return;

        if (e.isShiftClick()) {
            e.setCancelled(true);
        }

        if (e.getClickedInventory().getType() != InventoryType.PLAYER)
            return;
        if (e.getCurrentItem() == null)
            return;

        if (e.getSlot() == 36 || e.getSlot() == 37 || e.getSlot() == 38 || e.getSlot() == 39)
            e.setCancelled(true);

    }

}