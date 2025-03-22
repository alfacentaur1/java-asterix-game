package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.util.ArrayList;

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

    public void listInventory() {}
}
