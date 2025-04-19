package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.util.ArrayList;
import java.util.Optional;

public class Inventory {
    private static final int CAPACITY = 6;
    private static ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<Item>();
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }


    public void listInventory() {
        for (Item item : items) {
            System.out.println(item);
        }
    }
    public int getInventorySize() {
        return items.size();
    }

    //if mouseX and mouseY coord are near the item image AND player is near it too, we will add the item to inventory

    /**
     * searches for items near a specified location and the player.
     *
     * if an item is within a specified pixel range to both the mouse coordinates and the player,
     * it is returned. If the item is a potion, it is removed from the list.
     *
     * @param items      list of available items
     * @param x          mouse x click coordinates
     * @param y          mouse x click coordinates
     * @param tile_size  tile size(64 for now)
     * @param player     player character
     * @param inventory player's inventory - capacity = 6
     * @return the found item, or null if no item is nearby
     */
    public Item searchItems(ArrayList<Item> items, double x, double y, int tile_size, Asterix player,Inventory inventory) {
        if(inventory.getSize() >= CAPACITY) {
            return null;
        }
        for (Item item : items) {
            if (Math.abs(item.getX() - x) < tile_size / 2
                    && Math.abs(item.getY() - y) < tile_size /2
                    && Math.abs(item.getY() - (int) player.getY()) < tile_size / 3
                    && Math.abs(item.getX() - (int) player.getX()) < tile_size / 3)

            {
                if (item instanceof Potion) {
                    items.remove(item);
                }
                return item;
            }
        }
        return null;
    }
    public int getSize(){
        return items.size();
    }

    public ArrayList<Item> empty() {
        return new ArrayList<Item>();
    }

    public Item getItem(int pos) {
        return items.get(pos);
    }

}

