package com.kookykraftmc.kits;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by TimeTheCat on 7/4/2016.
 */
public class KitUtils {

    CatKits plugin;
    FileConfiguration cfg;

    KitUtils(CatKits plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getConfig();
    }

    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    void giveKit(String name, Player player) {
        //get config
        FileConfiguration cfg = this.plugin.getConfig();

        //get kit name
        String kitName = name;

        //get encoded string from config
        String es = (String) cfg.get("kit." + kitName);

        //check to make sure kit exists
        if (es != null && player.hasPermission("catkit.kit." + kitName)) {
            try {
                //get the kit from the base64
                Inventory inv = KitUtils.fromBase64(es);

                //get the items from the inventory and make sure they are not null
                ItemStack[] items = Arrays.stream(inv.getContents()).filter(i -> (i != null)).toArray(ItemStack[]::new);

                //get the max uses of the kit
                int maxUses = cfg.getInt("kit." + kitName + ".uses");

                //if the kit does not have a max use, give them the kit
                if (maxUses == 0 || maxUses == -1) {

                    player.getInventory().addItem(items);

                    player.sendMessage(ChatColor.GREEN + "You have recieved kit " + kitName);

                }
                //if the kit does have limit
                else if (maxUses > 0) {

                    //get the number of times the player used the kit
                    int usesByPlayer = cfg.getInt(player.getUniqueId() + "." + kitName + "." + "uses");

                    //if the number of times used is equal or greater than the maxuses, tell 'em they can't have it
                    if (usesByPlayer >= maxUses) {

                        player.sendMessage(ChatColor.RED + "You have already used that kit the maximum number of times.");

                    }
                    //else the player has less uses than the max, give em the kit
                    else {

                        //set the number of uses and save ot
                        cfg.set(player.getUniqueId() + "." + kitName + "." + "uses", ++usesByPlayer);
                        plugin.saveConfig();

                        //give them the items
                        player.getInventory().addItem(items);

                        //tell em they got it
                        player.sendMessage(ChatColor.GREEN + "You have recieved kit " + kitName);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(ChatColor.RED + "Kit does not exist or you do not have permission to use it.");
        }
    }

    void makeKit(String name, Player player) {
        makeKit(name, player, -1);
    }

    void makeKit(String name, Player player, int uses) {
        //get the kit name
        String kitName = name;

        //encode the player's inventory to a kit
        String eInv = KitUtils.toBase64((player.getInventory()));

        //get config
        FileConfiguration config = this.plugin.getConfig();

        //set the config option
        config.set("kit." + kitName, eInv);

        //set the kit uses
        config.set("kit." + kitName + ".uses", uses);

        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Kit " + kitName + " created.");
    }
}
