package com.kookykraftmc.kits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by TimeTheCat on 7/4/2016.
 */
public class KitCommand implements CommandExecutor {

    //private CatKits plugin = null;
    private KitUtils kitUtils = null;

    public KitCommand(KitUtils kt) {
        this.kitUtils = kt;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //if player has the permission to use kits and it's not console
        if (sender.hasPermission("catkits.use") && !(sender instanceof ConsoleCommandSender)) {
            //if no kit is provided
            if (args.length <= 0) {
                sender.sendMessage(cmd.getUsage());
            }
            //if kit is provided
            else if (args.length == 1) {
                kitUtils.giveKit(args[0], (Player) sender);
            }
            else {
                sender.sendMessage(ChatColor.RED + "Please provide a kit name.");

            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use kits.");
        }
        return true;
    }
}