package me.bumblebeee_.rpgdeath.commands;

import lombok.Getter;
import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.HiddenStringUtils;
import me.bumblebeee_.rpgdeath.util.Messages;
import me.bumblebeee_.rpgdeath.util.Runnables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TrovaanimaCommand implements CommandExecutor {

                            //Holder, Target
    private static @Getter Map<Player, Player> compasses = new HashMap<>();
    Random rand = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trovaanima")) {
            if (!sender.hasPermission("death.compass")) {
                sender.sendMessage(Messages.NO_PERMISSIONS.get());
                return false;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return false;
            }
            Player p = (Player) sender;
            Player t;

            if (!(args.length > 0)) {
                Optional<? extends Player> players = Bukkit.getServer().getOnlinePlayers().stream().filter(tp -> !tp.getUniqueId().equals(p.getUniqueId())).findAny();
                if (!players.isPresent()) {
                    sender.sendMessage(Messages.NO_PLAYERS_FOUND.get());
                    return false;
                }

                t = players.get();
            } else {
                t = Bukkit.getServer().getPlayer(args[0]);
                if (t == null) {
                    p.sendMessage(Messages.PLAYER_OFFLINE.get().replace("%player%", args[0]));
                    return false;
                }
            }

            int x;
            int y = t.getLocation().getBlockY();
            int z;

            int randX = rand.nextInt(RPGDeath.getInstance().getConfig().getInt("compass.wrong-x"));
            int randZ = rand.nextInt(RPGDeath.getInstance().getConfig().getInt("compass.wrong-z"));

            if (rand.nextBoolean()) {
                Location l = t.getLocation().add(randX,0,randZ);
                x = l.getBlockX();
                z = l.getBlockZ();
            } else {
                Location l = t.getLocation().subtract(randX,0,randZ);
                x = l.getBlockX();
                z = l.getBlockZ();
            }

            String display = RPGDeath.getInstance().getConfig().getString("compass.display");
            List<String> rawLore = RPGDeath.getInstance().getConfig().getStringList("compass.lore");
            List<String> lore = new ArrayList<>();
            for (String s : rawLore) {
                String msg = s.replace("%player%", t.getName());
                msg = msg.replace("%world%", t.getWorld().getName());
                msg = msg.replace("%x%", String.valueOf(x));
                msg = msg.replace("%y%", String.valueOf(y));
                msg = msg.replace("%z%", String.valueOf(z));
                lore.add(ChatColor.translateAlternateColorCodes('&', msg));
            }
            lore.add(HiddenStringUtils.encodeString("Track:" + t.getName()));

            ItemStack i = new ItemStack(Material.COMPASS);
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display.replace("%player%", t.getName())));
            im.setLore(lore);
            i.setItemMeta(im);

            p.getInventory().addItem(i);
            p.sendMessage(Messages.GIVEN_COMPASS.get().replace("%player%", t.getName()));
            p.setCompassTarget(t.getLocation());

            compasses.put(p, t);
            //world:x:y:z
            Runnables.getCoords().put(p.getUniqueId(), t.getWorld().getName() + ":" + x + ":" + y + ":" + z + ":" + t.getName());
            return true;
        }
        return false;
    }
}