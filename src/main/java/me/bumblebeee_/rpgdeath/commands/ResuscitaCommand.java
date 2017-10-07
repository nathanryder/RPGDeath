package me.bumblebeee_.rpgdeath.commands;

import com.github.games647.changeskin.bukkit.ChangeSkinBukkit;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Cooldowns;
import me.bumblebeee_.rpgdeath.util.Messages;
import me.bumblebeee_.rpgdeath.util.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ResuscitaCommand implements CommandExecutor {

    public static @Getter List<UUID> reviving = new ArrayList<>();
    public static @Getter Map<UUID, Location> notifications = new HashMap<>();

    Cooldowns cooldowns = new Cooldowns();
    Storage storage = new Storage();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("resuscita")) {
            if (!sender.hasPermission("death.revival")) {
                sender.sendMessage(Messages.NO_PERMISSIONS.get());
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return false;
            }
            Player p = (Player) sender;

            if (!(args.length > 0)) {
                sender.sendMessage(Messages.INVALID_ARGS.get().replace("%usage%", "/resuscita <player>"));
                return false;
            }

            Configuration config = RPGDeath.getInstance().getConfig();
            Player t = Bukkit.getServer().getPlayer(args[0]);
            String deathGroup = config.getString("deathGroup");
            if (t == null) {
                sender.sendMessage(Messages.PLAYER_OFFLINE.get().replace("%player%", args[0]));
                return false;
            }
            if (!RPGDeath.getPermission().playerInGroup(t, deathGroup)) {
                sender.sendMessage(Messages.NOT_DEAD.get().replace("%player%", t.getName()));
                return false;
            }
            if (reviving.contains(t.getUniqueId())) {
                sender.sendMessage(Messages.ALREADY_REVIVING.get().replace("%player%", t.getName()));
                return false;
            }

            if (t.getUniqueId().equals(p.getUniqueId())) {
                p.sendMessage(Messages.CANNOT_REVIVE_SELF.get());
                return false;
            }
            if (cooldowns.isPlayerActive(t.getUniqueId())) {
                p.sendMessage(Messages.PLAYER_IN_COOLDOWN.get().
                        replace("%player%", t.getName()).
                        replace("%time%", String.valueOf(cooldowns.getCooldown(t.getUniqueId())/60)));
                return false;
            }

            reviving.add(t.getUniqueId());
            notifications.put(t.getUniqueId(), p.getLocation());
            int delay = config.getInt("reviveDelay");
            Bukkit.getServer().getScheduler().runTaskLater(RPGDeath.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!reviving.contains(t.getUniqueId()))
                        return;

                    String reviveRegion = RPGDeath.getInstance().getConfig().getString("reviveRegion");
                    boolean insideRegion = isInRegion(t, reviveRegion);
                    if (!insideRegion) {
                        sender.sendMessage(Messages.RESURECT_FAILED.get());
                        t.sendMessage(Messages.RESURECT_FAILED.get());

                        reviving.remove(t.getUniqueId());
                        notifications.remove(t.getUniqueId());
                        notifications.remove(t.getUniqueId());

                        int cooldown = RPGDeath.getInstance().getConfig().getInt("resurrectCooldown");
                        cooldowns.addPlayer(t.getUniqueId(), cooldown*60);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!cooldowns.isPlayerActive(t.getUniqueId())) {
                                    this.cancel();
                                    return;
                                }

                                int time = Cooldowns.getCooldown().get(t.getUniqueId());
                                if (time <= 0) {
                                    cooldowns.removePlayer(t.getUniqueId());
                                    this.cancel();
                                    return;
                                }

                                cooldowns.addPlayer(t.getUniqueId(), time-1);
                            }
                        }.runTaskTimer(RPGDeath.getInstance(), 20, 20);
                        return;
                    }

                    String cmd = RPGDeath.getInstance().getConfig().getString("reviveCommand").replace("%player%", t.getName());
                    RPGDeath.getPermission().playerRemoveGroup(null, t, deathGroup);
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
                    t.teleport(notifications.get(t.getUniqueId()));

                    ChangeSkinBukkit cs = (ChangeSkinBukkit) Bukkit.getServer().getPluginManager().getPlugin("ChangeSkin");
                    cs.setSkin(t, t.getUniqueId(), true);

                    List<ItemStack> items = storage.getArmorItems(t.getUniqueId());
                    if (!(items == null) && items.size() > 0) {
                        for (ItemStack i : items) {
                            if (storage.getItemType(i).equalsIgnoreCase("helmet")) {
                                t.getInventory().setHelmet(i);
                            } else if (storage.getItemType(i).equalsIgnoreCase("chestplate")) {
                                t.getInventory().setChestplate(i);
                            } else if (storage.getItemType(i).equalsIgnoreCase("leggings")) {
                                t.getInventory().setLeggings(i);
                            } else if (storage.getItemType(i).equalsIgnoreCase("boots")) {
                                t.getInventory().setBoots(i);
                            } else {
                                RPGDeath.getInstance().getLogger().warning("Failed to find type of item called " + i.getType());
                            }
                        }
                    }

                    reviving.remove(t.getUniqueId());
                    reviving.remove(t.getUniqueId());
                    notifications.remove(t.getUniqueId());
                    notifications.remove(t.getUniqueId());
                }
            }, delay*20);

            Location l = notifications.get(t.getUniqueId());
            ConfigurationSection messageIDs = config.getConfigurationSection("messages");
            if (messageIDs == null)
                return false;

            for (String id : messageIDs.getKeys(false)) {
                String msg = config.getString("messages." + id + ".message");
                int time = config.getInt("messages." + id + ".delay");
                int range = config.getInt("messages." + id + ".range");

                Bukkit.getServer().getScheduler().runTaskLater(RPGDeath.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Entity e : l.getWorld().getNearbyEntities(l, range, range, range)) {
                            if (!(e instanceof Player))
                                continue;

                            e.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    }
                }, time*20);
            }

            //Particles
            if (config.getConfigurationSection("particles") == null)
                return false;

            int maxTime = 0;
            for (String id : config.getConfigurationSection("particles").getKeys(false)) {
                if (config.getInt("particles." + id + ".delay") > maxTime)
                    maxTime = config.getInt("particles." + id + ".delay");
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!reviving.contains(t.getUniqueId())) {
                        this.cancel();
                        return;
                    }

                    for (String id : config.getConfigurationSection("particles").getKeys(false)) {
                        List<String> particles = config.getStringList("particles." + id + ".particle");
                        int time = RPGDeath.getInstance().getConfig().getInt("particles." + id + ".delay");

                        for (String par : particles) {
                            Particle particle = Particle.valueOf(par);
                            if (particle == null) {
                                RPGDeath.getInstance().getLogger().warning("Failed to find particle named " + par);
                                continue;
                            }

                            Bukkit.getServer().getScheduler().runTaskLater(RPGDeath.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    l.getWorld().spawnParticle(particle, l, 100);
                                }
                            }, time*20);
                        }
                    }
                }
            }.runTaskTimer(RPGDeath.getInstance(), 0, maxTime*20);
        }
        return false;
    }

    public boolean isInRegion(Player p, String region) {
        ApplicableRegionSet set = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
        for (ProtectedRegion r : set) {
            if (r.getId().equalsIgnoreCase(region))
                return true;
        }

        return false;
    }
}