package com.kookykraftmc.kits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by TimeTheCat on 7/4/2016.
 */
public class KitCommand implements CommandExecutor {

    private CatKits plugin = null;

    public KitCommand(CatKits catKits) {
        this.plugin = catKits;
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

                //get config
                FileConfiguration cfg = this.plugin.getConfig();

                //get kit name
                String kitName = args[0].toLowerCase();

                //get encoded string from config
                String es = (String) cfg.get("kit." + kitName);

                //check to make sure kit exists
                if (es != null && sender.hasPermission("catkit.kit." + kitName)) {
                    try {
                        Inventory inv = KitUtils.fromBase64(es);
                        ItemStack[] items = Arrays.stream(inv.getContents()).filter(i -> (i != null)).toArray(ItemStack[]::new);
                        Player p = (Player) sender;
                        p.getInventory().addItem(items);
                        p.sendMessage(ChatColor.GREEN + "You have recieved kit " + kitName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Kit does not exist or you do not have permission to use it.");
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Please provide a kit name.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use kits.");
        }
        return true;
    }
}