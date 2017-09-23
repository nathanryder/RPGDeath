package me.bumblebeee_.rpgdeath.commands;

import me.bumblebeee_.rpgdeath.RPGDeath;
import me.bumblebeee_.rpgdeath.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("death")) {
            if (!sender.hasPermission("death.reload")) {
                sender.sendMessage(Messages.NO_PERMISSIONS.get());
                return false;
            }

            RPGDeath.getInstance().reloadConfig();
            sender.sendMessage(Messages.RELOAD_SUCCESS.get());
            return true;
        }
        return false;
    }
}