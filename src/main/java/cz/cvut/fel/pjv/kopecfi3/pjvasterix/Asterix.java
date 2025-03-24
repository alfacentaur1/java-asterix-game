package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class Asterix extends Character {
    private int attackPower = 1;
    private final int tile_size = ViewController.getTILE_SIZE();
    private final int map_width = ViewController.getMapWidth();
    private final int map_height = ViewController.getMapHeight();
    private Image playerImage;
    private int speed = 10;
    private long lastDamageTime = 0;
    private int manaCapacity = 5;
    private int mana = 0;

    public int getAttackPower() {
        return attackPower;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Image getPlayerImage() {
        return playerImage;
    }


    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public Asterix(double x, double y, int health) {
        super(10, 10, 10);
        this.playerImage = new Image(getClass().getResourceAsStream("/asterix.png"));
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
    //main function for moving asterix
    //we calculate new location based on speed input
    //then we check if asterix's new location won't be out of bounds or water or rock
    public void move(int dx, int dy, int[][] tileMap) {
        int new_x = (int) x + dx;
        int new_y = (int) y + dy;
        if (getCurrentTile(tileMap, (int) new_x - 5, (int) new_y - 5) != 1 && getCurrentTile(tileMap, new_x - 5, new_y - 5) != 3) {
            if (new_x >= -5 && new_x < map_width * tile_size && new_y >= -5 && new_y < map_height * tile_size) {
                x = new_x;
                y = new_y;
            }
        }
    }
    //function to get the tiles
    //we calculate x index from x coord + picture offset divided by tile
    //same y index to tilemap array
    //the tile index is not in bounds, we return 0
    public int getCurrentTile(int[][] tileMap, int x, int y) {
        int tileX = (x + 19) / tile_size;
        int tileY = (y + 26) / tile_size;

        if (tileX >= 0 && tileX < tileMap[0].length && tileY >= 0 && tileY < tileMap.length) {
            return tileMap[tileY][tileX];
        }

        return 0;
    }

    //loop through romans, decrement is for not to go out of bounds
    //if event on right mouse click happens and some romans/centurions are near, we will decrease their health
    //if their health is 0, we remove them from the arraylist of roman soldiers/centurions
    //if centurion is dead, it has 1/3 chance to drop the potion
    public ArrayList<Item> attack(Asterix player, ArrayList<RomanSoldier> romanSoldiers, ArrayList<Centurion> centurions, int TILE_SIZE, ArrayList<Item> items) {
        ArrayList<Item> newItems = new ArrayList<>(items);
        Random random = new Random();

        Iterator<RomanSoldier> romanIterator = romanSoldiers.iterator();
        while (romanIterator.hasNext()) {
            RomanSoldier r = romanIterator.next();
            if (Math.abs(r.getX() - player.getX()) < TILE_SIZE / 4 &&
                    Math.abs(r.getY() - player.getY()) < TILE_SIZE / 4) {
                for(int i = 0;i < this.getAttackPower();i++){
                    r.decreaseHealth();
                }
                r.decreaseHealth();
                if (r.getHealth() <= 0) {
                    romanIterator.remove();
                }
            }
        }

        Iterator<Centurion> centurionIterator = centurions.iterator();
        while (centurionIterator.hasNext()) {
            Centurion c = centurionIterator.next();
            if (Math.abs(c.getX() - player.getX()) < TILE_SIZE / 4 &&
                    Math.abs(c.getY() - player.getY()) < TILE_SIZE / 4) {
                for(int i = 0;i < this.getAttackPower();i++){
                    c.decreaseHealth();
                }
                if (c.getHealth() <= 0) {
                    double x = c.getX();
                    double y = c.getY();
                    centurionIterator.remove();

                    if (random.nextInt(2) == 0) {
                        Potion potion = new Potion(c.getX(), c.getY());
                        newItems.add(potion);
                    }
                }
            }
        }
        return newItems;
    }

    public void decreaseHealth() {
        health--;
    }

    //we loop through romans and centurions, if some of them is near and 2 sec cooldown is not up, we decrease asterix's health
    //roman - decrease once
    //centurion - decrease twice
    //if player is dead, end game
    public void checkForAttacks(Asterix player, ArrayList<RomanSoldier> romanSoldiers, ArrayList<Centurion> centurions,int TILE_SIZE) {
        long currentTime = System.currentTimeMillis();

        //cooldown 1 sec
        if (currentTime - lastDamageTime >= 2_000) {
            for (RomanSoldier r : romanSoldiers) {
                if (Math.abs(r.getX() - player.getX()) < TILE_SIZE / 4 &&
                        Math.abs(r.getY() - player.getY()) < TILE_SIZE / 4) {
                    player.decreaseHealth();
                    lastDamageTime = currentTime;

                    if (player.getHealth() < 1) {
                        System.exit(0) ;
                    }
                }
            }
            for (Centurion c : centurions) {
                if (Math.abs(c.getX() - player.getX()) < TILE_SIZE / 4 &&
                        Math.abs(c.getY() - player.getY()) < TILE_SIZE / 4) {
                    player.decreaseHealth();
                    player.decreaseHealth();
                    lastDamageTime = currentTime;

                    if (player.getHealth() < 1) {
                        System.exit(0) ;
                    }
                }
            }
        }
    }
    //on pressing T - save inventory
    public void saveInventory(Inventory inventory) {
        int carrotCounter = 0;
        int waterBucketCounter = 0;
        int shroomCounter = 0;
        for (Item item : inventory.getItems()) {
            if(item instanceof WaterBucket) {
                waterBucketCounter++;
            }
            if(item instanceof Shroom) {
                shroomCounter++;
            }
            if(item instanceof Carrot) {
                carrotCounter++;
            }
        }
        try (FileWriter writer = new FileWriter("src/main/resources/inventoryAsterix")) {
            writer.write("Carrot " + carrotCounter + "\n");
            writer.write("WaterBucket " + waterBucketCounter + "\n");
            writer.write("Shroom " + shroomCounter + "\n");
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.err.println("Error while saving inventory: " + e.getMessage());
        }

    }
    public void loadInventory(Inventory inventory) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/inventoryAsterix"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) continue;

                String itemToAdd = parts[0];
                int count = Integer.parseInt(parts[1]);

                for (int i = 0; i < count; i++) {
                    switch (itemToAdd) {
                        case "Carrot" -> inventory.addItem(new Carrot(0, 0));
                        case "WaterBucket" -> inventory.addItem(new WaterBucket(0, 0));
                        case "Shroom" -> inventory.addItem(new Shroom(0, 0));
                    }
                }
            }
            System.out.println("Inventory loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error while loading inventory: " + e.getMessage());
        }
    }}








