package com.miyuki.chestexchangeshop;

import com.miyuki.chestexchangeshop.core.listeners.PlayerCreateShop;
import com.miyuki.chestexchangeshop.core.listeners.PlayerOpenShop;
import com.miyuki.chestexchangeshop.core.listeners.ProtectShop;
import com.miyuki.chestexchangeshop.core.manager.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import sun.security.krb5.Config;

import java.io.File;

public final class ChestExchangeShop extends JavaPlugin {
    private static ShopManager shopManager = null;
    private static File data_file = null;

    public static ChestExchangeShop plugin;


    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigInt();
        plugin = this;
        shopManager = new ShopManager(this);
        Bukkit.getPluginManager().registerEvents(new PlayerCreateShop(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerOpenShop(),this);
        Bukkit.getPluginManager().registerEvents(new ProtectShop(),this);

        getLogger().info("Load ChestExchangeShop Success!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Initialize config.yml
     */
    private void ConfigInt(){
        saveDefaultConfig();
        data_file = new File(getDataFolder(),"data");
        if(!data_file.isDirectory() || data_file.exists()){
            data_file.mkdir();
        }
    }

    /**
     * Get Data folder
     * @return File Object
     */
    public static File getDataFile(){
        return data_file;
    }


    /**
     * Get ShopManager Object
     * @return ShopManager Object
     */
    public static ShopManager getShopManager(){
        return shopManager;
    }
}
