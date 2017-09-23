package me.bumblebeee_.rpgdeath.listeners;

import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class PlayerRespawn implements Listener {

    Random r = new Random();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        String rawWorld = RPGDeath.getInstance().getConfig().getString("respawnWorld");
        World w = Bukkit.getServer().getWorld(rawWorld);
        Player p = e.getPlayer();
        String deathGroup = RPGDeath.getInstance().getConfig().getString("deathGroup");

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