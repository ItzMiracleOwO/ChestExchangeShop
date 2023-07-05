package com.miyuki.chestexchangeshop.core.listeners;

import com.miyuki.chestexchangeshop.core.Utils;
import com.miyuki.chestexchangeshop.core.api.Shop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import static com.miyuki.chestexchangeshop.ChestExchangeShop.*;

public class PlayerCreateShop implements Listener {
    @EventHandler
    public void onListening(SignChangeEvent event){

        // Check if the chest is shop
        Block block = Shop.getAttache(event.getBlock());
        if(block == null){
            return;
        }
        if (block.getBlockData().getMaterial() != Material.CHEST) {
            return;
        }
        if (getShopManager().getChestShop((Chest) block.getState())!=null) {
            return;
        }


        // Check sign match
        String[] msg = event.getLines();
        if(msg[0].isEmpty() || msg[1].isEmpty()){
            return;
        }
        if(!msg[0].toLowerCase().equals(plugin.getConfig().getString("title").toLowerCase())){
            return;
        }


        // Check item count
        String[] need = msg[1].split(" ");
        try{
            Integer.valueOf(need[0]);
        }catch (NumberFormatException e){
            event.getPlayer().sendMessage("§cFailed to create shop, wrong item amount");
        }
        Material material = Material.getMaterial(need[1].toUpperCase());
        if(material==null){
            event.getPlayer().sendMessage("§cFailed to create shop, the item does not exist");
            return;
        }
        event.getPlayer().sendMessage("§aShop created successfully.");
        event.setLine(0,"§1[SHOP]");
        event.setLine(2,event.getPlayer().getName());
        ((Sign)event.getBlock().getState()).setEditable(false);
        getShopManager().register(Utils.getlinkNo(),event.getBlock(),event.getPlayer().getUniqueId(),event.getLines());
    }


}
