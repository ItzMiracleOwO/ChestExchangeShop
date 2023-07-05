package com.miyuki.chestexchangeshop.core.listeners;

import com.miyuki.chestexchangeshop.core.api.Shop;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static com.miyuki.chestexchangeshop.ChestExchangeShop.getShopManager;

public class PlayerOpenShop implements Listener {
    @EventHandler
    public void onListening(InventoryClickEvent event){
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack==null){
            return;
        }
        if(itemStack.getType() == Material.AIR){
            return;
        }



        // Check if the chest is shop
        if(event.getClickedInventory().getType() != InventoryType.CHEST){
            return;
        }
        Shop shop = getShopManager().getChestShop((Chest) event.getInventory().getLocation().getBlock().getState());
        if (shop==null) {
            return;
        }

        if(event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_SOME){
            event.setCancelled(true);
            return;
        }



        // Check if the player is owner
        if(shop.getOwner().equals(event.getWhoClicked().getUniqueId())){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);


        if(shop.getNeed().isSimilar(itemStack)){
            player.sendMessage("§cYou cannot buy this item!");
            player.closeInventory();
            return;
        }

        // Check if the player has enough money
        if(!Shop.hasItems(player.getInventory(),shop.getNeed())){
            player.sendMessage("§cYou do not have enough item to buy this!");
            player.closeInventory();
            return;
        }


        // Check if the player has enough space
        if(!Shop.hasSpace(itemStack,player.getInventory(),itemStack.getAmount())){
            player.sendMessage("§cThere are no enough space in your inventory!");
            return;
        }



        // Put money into chest
        Shop.addItems(shop.getChest().getInventory(), shop.getNeed(),shop.getAmount());

        // Remove money from player
        Shop.removeItems(player.getInventory(),shop.getNeed(),shop.getAmount());

        // Remove item from shop
        event.getInventory().setItem(event.getSlot(),new ItemStack(Material.AIR));

        // Give the item to the player
        Shop.addItems(player.getInventory(),itemStack,itemStack.getAmount());
        shop.update();
        player.sendMessage("§aSuccessfully buy item!");
        

    }
}
