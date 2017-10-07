package me.bumblebeee_.rpgdeath.util;

import lombok.Getter;
import me.bumblebeee_.rpgdeath.RPGDeath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Runnables {

    Random rand = new Random();

    //world:x:y:z
    private static @Getter Map<UUID, String> coords = new HashMap<>();

    public void compassUpdater() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(RPGDeath.getInstance(), new Runnable() {
            @Override
            public void run() {
                //TODO this and redo rightclick
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    for (ItemStack i : p.getInventory().getContents()) {
                        if (i == null)
                            continue;
                        if (i.getType() != Material.COMPASS)
                            continue;
                        if (!i.hasItemMeta())
                            continue;
                        if (!i.getItemMeta().hasDisplayName())
                            continue;
                        int hiddenIndex = i.getItemMeta().getLore().size()-1;
                        if (!HiddenStringUtils.hasHiddenString(i.getItemMeta().getLore().get(hiddenIndex)))
                            continue;

                        String lore = HiddenStringUtils.extractHiddenString(i.getItemMeta().getLore().get(hiddenIndex));
                        if (lore.split(":").length < 2)
                            continue;
                        if (!lore.split(":")[0].equalsIgnoreCase("Track"))
                            continue;
                        Player t = Bukkit.getServer().getPlayer(lore.split(":")[1]);
                        if (t == null)
                            continue;

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

                        List<String> rawLore = RPGDeath.getInstance().getConfig().getStringList("compass.lore");
                        List<String> newLore = new ArrayList<>();
                        for (String s : rawLore) {
                            String msg = s.replace("%player%", t.getName());
                            msg = msg.replace("%world%", t.getWorld().getName());
                            msg = msg.replace("%x%", String.valueOf(x));
                            msg = msg.replace("%y%", String.valueOf(y));
                            msg = msg.replace("%z%", String.valueOf(z));
                            newLore.add(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                        newLore.add(HiddenStringUtils.encodeString("Track:" + t.getName()));

                        ItemMeta im = i.getItemMeta();
                        im.setLore(newLore);
                        i.setItemMeta(im);
                        Runnables.getCoords().put(p.getUniqueId(), t.getWorld().getName() + ":" + x + ":" + y + ":" + z  + ":" + t.getName());
                    }
                }
            }
        }, 40, 40);
    }
}
