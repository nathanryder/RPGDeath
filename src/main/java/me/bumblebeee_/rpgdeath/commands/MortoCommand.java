package me.bumblebeee_.rpgdeath.commands;

import me.bumblebeee_.rpgdeath.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MortoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("morto")) {
            if (!sender.hasPermission("death.kill")) {
                sender.sendMessage(Messages.NO_PERMISSIONS.get());
                return false;
            }

            if (!(args.length > 0)) {
                sender.sendMessage(Messages.INVALID_ARGS.get().replace("%usage%", "/morto <player>"));
                return false;
            }

            Player t = Bukkit.getServer().getPlayer(args[0]);
            if (t == null) {
                sender.sendMessage(Messages.PLAYER_OFFLINE.get().replace("%player%", args[0]));
                return false;
            }

            t.setHealth(0);
            return true;
        }
        return false;
    }
}