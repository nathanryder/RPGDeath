package me.bumblebeee_.rpgdeath.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))
            return;

        Player p = (Player) e.getDamager();
        if (!p.hasPermission("death.death"))
            return;

        if (isWeapon(p.getInventory().getItemInMainHand())) {
            e.setCancelled(true);
        }
    }

    public boolean isWeapon(ItemStack is) {
        if (is == null)
            return false;

        Material type = is.getType();
        if (type == Material.AIR)
            return false;

        switch (type) {
            case DIAMOND_SWORD:
                return true;
            case GOLD_SWORD:
                return true;
            case IRON_SWORD:
                return true;
            case STONE_SWORD:
                return true;
            case WOOD_SWORD:
                return true;
            case BOW:
                return true;
            case DIAMOND_AXE:
                return true;
            case GOLD_AXE:
                return true;
            case IRON_AXE:
                return true;
            case STONE_AXE:
                return true;
            case WOOD_AXE:
                return true;
            default:
                return false;
        }
    }

}