package com.miyuki.chestexchangeshop.core.listeners;

import com.miyuki.chestexchangeshop.ChestExchangeShop;
import com.miyuki.chestexchangeshop.core.api.Shop;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.checkerframework.checker.units.qual.C;

public class ProtectShop implements Listener {
    @EventHandler
    public void onListening(BlockBreakEvent event){
        Block block = event.getBlock();
        if(block.getBlockData() instanceof WallSign){
            Sign sign = (Sign) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getSignShop(sign);
            if(shop == null){
                return;
            }
            if(!shop.getOwner().equals(event.getPlayer().getUniqueId())){
                event.getPlayer().sendMessage("§cCannot remove shop, this is not your shop!");
                event.setCancelled(true);
                return;

            }

            ChestExchangeShop.getShopManager().removeShop(shop);
            event.getPlayer().sendMessage("§aRemoved shop");
        }else if (block.getBlockData().getMaterial() == Material.CHEST){

           Chest chest = (Chest) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getChestShop(chest);
            if(shop == null){
                return;
            }

            if(!shop.getOwner().equals(event.getPlayer().getUniqueId())){
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cCannot remove shop, this is not your shop!");
                return;

            }

            ChestExchangeShop.getShopManager().removeShop(shop);
            event.getPlayer().sendMessage("§aRemoved shop");
        }
    }

    @EventHandler
    public void onListening(BlockExplodeEvent event){
        Block block = event.getBlock();
        if(block.getBlockData() instanceof WallSign){
            Sign sign = (Sign) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getSignShop(sign);
            if(shop != null){
                event.setCancelled(true);
            }

        }else if (block.getBlockData().getMaterial()==Material.CHEST){
            Chest chest = (Chest) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getChestShop(chest);
            if(shop != null){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onListening(InventoryMoveItemEvent event){
        if(event.getSource().getLocation()==null){
            return;
        }
        Block block = event.getSource().getLocation().getBlock();

        if (block.getBlockData().getMaterial()==Material.CHEST){
            Chest chest = (Chest) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getChestShop(chest);
            if(shop == null){
                return;
            }
            if(block.getRelative(BlockFace.DOWN) instanceof Hopper){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onListening(BlockBurnEvent event){
        Block block = event.getBlock();
        if(block.getBlockData() instanceof WallSign){
            Sign sign = (Sign) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getSignShop(sign);
            if(shop != null){
                event.setCancelled(true);
            }

        }else if (block.getBlockData().getMaterial()==Material.CHEST){
            Chest chest = (Chest) block.getState();
            Shop shop = ChestExchangeShop.getShopManager().getChestShop(chest);
            if(shop != null){
                event.setCancelled(true);
            }
        }
    }




}
