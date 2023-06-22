package com.miyuki.chestexchangeshop.core.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ListIterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Shop Class
 */
public class Shop {

    /**
     * Shop owner UUID
     */
    private UUID owner;

    /**
     * Shop owner name
     */
    private String name;

    /**
     * Shop Location (Sign)
     */
    private Location sign_location;

    /**
     * Shop Location (Chest)
     */
    private Location chest_location;

    /**
     * Shop sign
     */
    private Sign sign;

    /**
     * Shop chest
     */
    private Chest chest;

    /**
     * Needed item
     */
    private ItemStack need;

    /**
     * Shop name
     */
    private String shop_name;
    /**
     * Initialize shop
     * @param location Shop Location (Sign)
     */
    public Shop(String name,Location location,UUID owner,String[] msg){
        this.shop_name =name;
        this.sign_location = location;
        this.sign = (Sign)location.getBlock().getState();
        this.chest = (Chest)getAttache(this.sign.getBlock()).getState();
        this.owner = owner;
        this.name = Bukkit.getOfflinePlayer(owner).getName();
        this.chest_location = chest.getLocation();

        String[] need = msg[1].split(" ");
        Material material = Material.getMaterial(need[1].toUpperCase());
        ItemStack stack = new ItemStack(material);
        stack.setAmount(Integer.valueOf(need[0]));
        this.need = stack;
    }


    /**
     * Initialize shop
     * @param location Shop Location (Sign)
     */
    public Shop(String name,Location location,UUID owner){
        this.shop_name =name;
        this.sign_location = location;
        this.sign = (Sign)location.getBlock().getState();
        this.chest = (Chest)getAttache(this.sign.getBlock()).getState();
        this.owner = owner;
        this.name = Bukkit.getOfflinePlayer(owner).getName();
        this.chest_location = chest.getLocation();

        String[] need = sign.getLine(1).split(" ");
        Material material = Material.getMaterial(need[1].toUpperCase());
        ItemStack stack = new ItemStack(material);
        stack.setAmount(Integer.valueOf(need[0]));
        this.amount = Integer.valueOf(need[0]);
        this.need = stack;
    }
    public int getAmount(){
        return this.amount;
    }

    /**
     * Set shop name
     */
    public void setName(String name){
        this.shop_name = name;
    }


    /**
     * Get shop name
     */
    public String getName(){
        return this.shop_name;
    }
    /**
     * Get needed item
     * @return item
     */
    public ItemStack getNeed(){
        return this.need;
    }

    /**
     * Get chest
     * @return chest Object
     */
    public Chest getChest(){
        return this.chest;
    }

    /**
     * Get sign location
     * @return sign location
     */
    public Location getSignLocation(){
        return this.sign_location;
    }


    /**
     * Get sign
     * @return sign
     */
    public Sign getSign(){
        return this.sign;
    }

    /**
     * Get shop owner
     * @return owner UUID
     */
    public UUID getOwner(){
        return this.owner;
    }

    /**
     * Get shop owner name
     * @return shop owner name
     */
    public String getOwnerName(){
        return this.name;
    }

    /**
     * Get chest location
     * @return Chest location
     */
    public Location getChestLocation(){
        return this.chest_location;
    }

    /**
     * Amount
     */
    private int amount;

    /**
     * Get sign associated chest
     * @param b sign
     * @return chest
     */
    public static Block getAttache(Block b) {
        if (b == null)
            return null;
        if (!(b.getState().getBlockData() instanceof WallSign))
            return null;
        WallSign wallSign = (WallSign)b.getState().getBlockData();
        return b.getRelative(wallSign.getFacing().getOppositeFace());
    }


    /**
     * Add items to inventory
     * @param inventory Inventory
     * @param itemStack Item
     * @param amount Amount
     */
    public static void addItems(Inventory inventory, ItemStack itemStack, int amount) {
        itemStack.setAmount(1);
        for (int i = 0; i < amount; i++) {
            inventory.addItem(new ItemStack[] { itemStack });
        }
    }


    /**
     * Check inventory has space
     * @param is Item
     * @param inventory Inventory
     * @param amount Amount
     * @return true if has space, else false
     */
    public static boolean hasSpace(ItemStack is, Inventory inventory, int amount) {
        int slots = (int)Math.ceil((amount / is.getMaxStackSize()));
        if (amount % is.getMaxStackSize() != 0)
            slots++;
        if (slots == 0)
            slots = 1;
        return (getEmptySlots(inventory) >= slots);
    }

    /**
     * Get empty inventory count
     * @param inventory Inventory
     * @return empty inventory count
     */
    public static int getEmptySlots(Inventory inventory) {
        int amount = 0;
        for (ItemStack is : inventory.getStorageContents()) {
            if (is == null || is.getType() == Material.AIR)
                amount++;
        }
        return amount;
    }


    /**
     * Check inventory has items
     * @param inventory inventory
     * @param itemStack item
     * @return true if has items, else false
     */
    public static boolean hasItems(Inventory inventory,ItemStack itemStack){
        if(itemStack==null){
            return false;
        }
        return getItemAmount(inventory, itemStack) >= itemStack.getAmount();
    }


    /**
     * Swaping item
     * @param inventory inventory
     * @param itemStack item
     * @param amount amount
     */
    public static void removeItems(Inventory inventory, ItemStack itemStack, int amount) {
        itemStack.setAmount(1);
        for (int i = 0; i < amount; i++) {
            for (ListIterator<ItemStack> listIterator = inventory.iterator(); listIterator.hasNext(); ) {
                ItemStack invItem = listIterator.next();
                if (invItem != null && invItem.isSimilar(itemStack)) {
                    invItem.setAmount(invItem.getAmount() - 1);
                    break;
                }
            }
        }
    }


    /**
     * Get item in inventory
     * @param inventory inventory
     * @param itemStack item
     * @return amount
     */
    public static int getItemAmount(Inventory inventory, ItemStack itemStack) {
        int amount = 0;
        for (ListIterator<ItemStack> listIterator = inventory.iterator(); listIterator.hasNext(); ) {
            ItemStack item = listIterator.next();
            if (item != null && item.isSimilar(itemStack))
                amount += item.getAmount();
        }
        return amount;
    }

    public void update(){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
        if(offlinePlayer.getName()==null){
            return;
        }
        if(!sign.getLine(2).equals(offlinePlayer.getName())){
            sign.setLine(2,offlinePlayer.getName());
        }
    }
}
