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
public class CreateKitCommand implements CommandExecutor {

    KitUtils kitUtils = null;

    public CreateKitCommand(KitUtils kitUtils) {
        this.kitUtils = kitUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //if player can create kits and is not console
        if (sender.hasPermission("catkits.createkit") && !(sender instanceof ConsoleCommandSender)) {
            //if no kit name is provided
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.RED + cmd.getUsage());

            }
            //if kit name is provided
            else if (args.length == 1) {
                kitUtils.makeKit(args[0], (Player) sender);
            }
            //if the kit has max uses
            else if (args.length == 2) {
                kitUtils.makeKit(args[0], (Player) sender, Integer.parseInt(args[1]));
            }
            //if nothing else matches
            else {
                sender.sendMessage(ChatColor.RED + cmd.getUsage());
            }
        }
        //if sender does not have perms to use command
        else {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to use that command!");
            return true;
        }
        return true;
    }
}