package com.kookykraftmc.kits;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by TimeTheCat on 7/3/2016.
 */
public class CatKits extends JavaPlugin {

    Logger l = Logger.getLogger("CatKits");
    KitUtils kt = null;

    @Override
    public void onEnable() {
        l.info("Loading config...");
        File config = new File(getDataFolder(), "config.yml");

        if(!config.exists()) {
            l.info("No config! D:, let's change that...");
            l.info("Making config...");
            saveDefaultConfig();
        } else if (this.getConfig().getString("no-touchy").isEmpty() || !this.getDescription().getVersion().equals("no-touchy")) {
            l.info("Your config is out of date! Saving updated version...");
            saveDefaultConfig();
        }

        l.info("Loading KitUtils...");
        kt = new KitUtils(this);

        l.info("Loading commands...");
        getCommand("kit").setExecutor(new KitCommand(kt));
        getCommand("createkit").setExecutor(new CreateKitCommand(kt));
    }

    KitUtils getKitUtils() {
        return kt;
    }


}
