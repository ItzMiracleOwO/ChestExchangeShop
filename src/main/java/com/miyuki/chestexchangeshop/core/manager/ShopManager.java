package com.miyuki.chestexchangeshop.core.manager;

import com.miyuki.chestexchangeshop.ChestExchangeShop;
import com.miyuki.chestexchangeshop.core.Utils;
import com.miyuki.chestexchangeshop.core.api.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import static com.miyuki.chestexchangeshop.ChestExchangeShop.*;

public class ShopManager {

    private List<Shop> players_shop= new ArrayList<>();


    private ChestExchangeShop plugin;
    private File data_file = null;


    /**
     * Initialize ShopManager
     */
    public ShopManager(ChestExchangeShop chestExchangeShop){
        this.plugin = chestExchangeShop;
        this.data_file = getDataFile();;

        // Load shop from data

        String uuid = "";
        World world;
        Block block = null;
        YamlConfiguration sub_data;

        for(File i: Objects.requireNonNull(data_file.listFiles())){
            uuid =  i.getName().replace(".yml","");
            sub_data = YamlConfiguration.loadConfiguration(i);
            for(String t : sub_data.getKeys(false)){
                world = Bukkit.getWorld(sub_data.getString(t+".Location.world"));

                // Block-------------
                block = world.getBlockAt(
                        new Location(
                                world,sub_data.getDouble(t+".Location.x"),
                                sub_data.getDouble(t+".Location.y"),
                                sub_data.getDouble(t+".Location.z")
                        )
                );

                // Block-------------

                if(!(block.getBlockData() instanceof WallSign)){
                    sub_data.set(t+".Location",null);
                }else {
                    register(t,block,UUID.fromString(uuid));
                }


            }

            try {
                sub_data.save(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Register shop (Internal)
     * @param block Shop location (Sign)
     * @param uuid Shop owner (UUID)
     */
    private void register(String name,Block block,UUID uuid){
        Shop shop = new Shop(name,block.getLocation(),uuid);
        if(hasShop(shop)){
            return;
        }
        this.players_shop.add(shop);
    }

    /**
     * Get shop's sign
     * @return shop
     */
    public Shop getChestShop(Chest chest){
        for(Shop i :players_shop){
            if(i.getChest().equals(chest)){
                return i;
            }
        }
        return null;
    }

    /**
     * Get sign's shop
     */
    public Shop getSignShop(Sign sign){
        for(Shop i :players_shop){
            if(i.getSign().equals(sign)){
                return i;
            }
        }
        return null;
    }

    /**
     * Register shop (External)
     * @param block Block location (Sign)
     * @param uuid UUID
     * @param msg message
     */
    public void register(String namew,Block block,UUID uuid,String[] msg){
        Shop shop = new Shop(namew,block.getLocation(),uuid,msg);
        if(hasShop(shop)){
            return;
        }
        this.players_shop.add(shop);
        File uuid_data_file = new File(data_file,uuid.toString() + ".yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(uuid_data_file);

        String name = shop.getName();
        while (true){
            if(yamlConfiguration.getKeys(false).contains(name)){
                name = Utils.getlinkNo();
            }else {
                break;
            }
        }
        shop.setName(name);

        yamlConfiguration.set(name+".Location.world",block.getWorld().getName());
        yamlConfiguration.set(name+".Location.x",block.getLocation().getX());
        yamlConfiguration.set(name+".Location.y",block.getLocation().getY());
        yamlConfiguration.set(name+".Location.z",block.getLocation().getZ());
        try {
            yamlConfiguration.save(uuid_data_file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Check if shop exisits
     * @param shop shop Object
     * @return true if shop exisits, else false
     */
    public boolean hasShop(Shop shop){
        return players_shop.contains(shop);
    }


    /**
     * Get all shop of a player
     * @return All shop of the player
     */
    public List<Shop> getPlayerShop(UUID uuid){
        List<Shop> shops = new ArrayList<>();
        for(Shop i : players_shop){
            if(uuid == i.getOwner()){
                shops.add(i);
            }
        }

        return shops;
    }

    /**
     * Get all player's shop
     * @return All player's shop
     */
    public List<Shop> getPlayersShop(){
        return this.players_shop;
    }


    /**
     * Remove shop
     */
    public void removeShop(Shop shop){

        if(!hasShop(shop)){
            return;
        }
        players_shop.remove(shop);
        File file = new File(data_file,shop.getOwner().toString() + ".yml");
        if(!file.exists()){
            return;
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set(shop.getName(),null);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
