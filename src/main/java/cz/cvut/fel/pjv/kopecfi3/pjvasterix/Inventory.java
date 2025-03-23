package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.util.ArrayList;
import java.util.Optional;

public class Inventory {
    private static final int CAPACITY = 7;
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

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void listInventory() {
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public Item searchItems(ArrayList<Item> items, double x, double y, int tile_size, Asterix player) {
        for (Item item : items) {
            if (Math.abs(item.getX() - x) < tile_size / 2
                    && Math.abs(item.getY() - y) < tile_size /2
                    && Math.abs(item.getY() - (int) player.getY()) < tile_size / 3
                    && Math.abs(item.getX() - (int) player.getX()) < tile_size / 3)

            {
                System.out.println("returning item");
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

}

