package me.bumblebeee_.rpgdeath.listeners;

import com.github.games647.changeskin.bukkit.ChangeSkinBukkit;
import com.github.games647.changeskin.core.ChangeSkinCore;
import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Messages;
import me.bumblebeee_.rpgdeath.util.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class PlayerRespawn implements Listener {

    Random r = new Random();
    Storage storage = new Storage();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        String rawWorld = RPGDeath.getInstance().getConfig().getString("respawnWorld");
        World w = Bukkit.getServer().getWorld(rawWorld);
        Player p = e.getPlayer();
        String deathGroup = RPGDeath.getInstance().getConfig().getString("deathGroup");

        if (p.hasPermission("death.bypass"))
            return;

        if (w == null) {
            p.sendMessage(ChatColor.RED + "Failed to find world!");
            RPGDeath.getInstance().getLogger().severe("Failed to respawn player to unknown world named " + rawWorld);
            return;
        }

        Bukkit.getServer().getScheduler().runTaskLater(RPGDeath.getInstance(), new Runnable() {
            @Override
            public void run() {
                Location loc = getRandomLocation(p, w);

                while (loc.getBlock().getLocation().add(0,1,0).getBlock().isLiquid())
                    loc = getRandomLocation(p, w);

                p.teleport(loc);
                RPGDeath.getPermission().playerAddGroup(null, p, deathGroup);

                UUID uuid = UUID.fromString(RPGDeath.getInstance().getConfig().getString("deathSkinUUID"));
                ChangeSkinBukkit cs = (ChangeSkinBukkit) Bukkit.getServer().getPluginManager().getPlugin("ChangeSkin");
                cs.setSkin(p, uuid, true);

                if (p.hasPermission("rpginventory.keep.items") ||
                        p.hasPermission("rpginventory.keep.armor") ||
                        p.hasPermission("rpginventory.keep.rpginv")) {

                    for (ItemStack item : p.getInventory().getArmorContents()) {
                        storage.addArmorItem(uuid, item);
                    }
                }
                p.getInventory().setArmorContents(null);

            }
        }, 10);

        int delay = RPGDeath.getInstance().getConfig().getInt("respawnMessageDelay");
        Bukkit.getServer().getScheduler().runTaskLater(RPGDeath.getInstance(), new Runnable() {
            @Override
            public void run() {
                p.sendMessage(Messages.RESPAWN_MSG_ONE.get().replace("%player%", p.getDisplayName()));
                p.sendMessage(Messages.RESPAWN_MSG_TWO.get().replace("%player%", p.getDisplayName()));
            }
        }, delay*20);
    }

    public Location getRandomLocation(Player p, World w) {
        int radius = RPGDeath.getInstance().getConfig().getInt("respawnRadius");
        int x;
        int z;
        if (r.nextBoolean()) {
            x = r.nextInt(radius)-radius;
            z = r.nextInt(radius)-radius;
        } else {
            x = r.nextInt(radius);
            z = r.nextInt(radius);
        }
        int y = p.getWorld().getHighestBlockYAt(new Location(p.getWorld(), x, 200, z));
        return new Location(w, x, y, z, p.getLocation().getYaw(), p.getLocation().getPitch());
    }
}