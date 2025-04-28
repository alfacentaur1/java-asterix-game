package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Asterix extends Character {
    private static final Logger logger = Logger.getLogger(Asterix.class.getName());

    private int attackPower = 1;
    private boolean hasBoots = false;
    private final int tile_size = ViewController.getTILE_SIZE();
    private final int map_width = ViewController.getMapWidth();
    private final int map_height = ViewController.getMapHeight();
    private Image playerImage;
    private int speed = 5;
    private long lastDamageTime = 0;
    private int manaCapacity = 5;
    private int mana = 5;
    private GameState gameState = GameState.RUNNING;

    public int getAttackPower() {
        return attackPower;
    }

    public int getSpeed() {
        return speed;
    }
    public int getMana(){
        return mana;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public boolean hasBoots(){
        return hasBoots;
    }

    public void setBoots(boolean hasBoots){
        this.hasBoots = hasBoots;
    }

    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public int getAttackpower(){
        return 3;
    }

    public Asterix(double x, double y, int health) {
        super((int)x, (int)y, 10);
        this.playerImage = new Image(getClass().getResourceAsStream("/asterix.png"));
    }


    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    /**
     * main function for moving asterix
     * we calculate new location based on speed input
     * then we check if asterix's new location won't be out of bounds or water or rock
     * @param dx current player's position x
     * @param dy current player's position y
     * @param tileMap current loaded map
     */
    public void move(int dx, int dy, int[][] tileMap) {
        int new_x = (int) x + dx;
        int new_y = (int) y + dy;
        int current_tile = getCurrentTile(tileMap, (int) new_x - 5, (int) new_y - 5);
        if (current_tile != 1 && current_tile != 3) {
            if (new_x >= -5 && new_x < map_width * tile_size && new_y >= -5 && new_y < map_height * tile_size) {
                if(current_tile == 4){
                    if(hasBoots){
                        x = new_x;
                        y = new_y;
                    }
                }
                else if(current_tile != 4){
                    x = new_x;
                    y = new_y;
                }
            }
        }
    }
    //function to get the tiles
    //we calculate x index from x coord + picture offset divided by tile
    //same y index to tilemap array
    //

    /**
     * function to get the tiles
     * we calculate x index from x coord + picture offset divided by tile
     * same y index to tilemap array
     * the tile index is not in bounds, we return 0
     * @param tileMap current loaded map
     * @param x current player's position x
     * @param y current player's position y
     * @return number in tilemap
     */
    public int getCurrentTile(int[][] tileMap, int x, int y) {
        int tileX = (x + 13) / tile_size;
        int tileY = (y + 30) / tile_size;

        if (tileX >= 0 && tileX < tileMap[0].length && tileY >= 0 && tileY < tileMap.length) {
            return tileMap[tileY][tileX];
        }

        return 0;
    }


    /**
     *loop through romans, decrement is for not to go out of bounds
     * attack only if asterix's mana is > 0
     * if event on right mouse click happens and some romans/centurions are near, we will decrease their health
     * if their health is 0, we remove them from the arraylist of roman soldiers/centurions
     * if centurion is dead, it has 1/3 chance to drop the potion
     * @param player      character performing the attack
     * @param romanSoldiers list of Roman soldiers currently in the game
     * @param centurions  list of centurions currently in the game
     * @param TILE_SIZE    tile size used to determine attack range
     * @param items        list of items currently available in the game
     * @return a new list of items including any potions dropped by defeated centurions
     */
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

//                    if (random.nextInt(2) == 0) {
                        Potion potion = new Potion(c.getX(), c.getY());
                        newItems.add(potion);
//                    }
                }
            }
        }
        decreaseMana(player);
        return newItems;
    }

    public void decreaseHealth() {
        health--;
    }

    //we loop through romans and centurions, if some of them is near and 2 sec cooldown is not up, we decrease asterix's health
    //roman - decrease once
    //centurion - decrease twice
    //if player is dead, end game

    /**
     *
     * @param player       player character (Asterix) being checked for attacks
     * @param romanSoldiers list of Roman soldiers currently in the game
     * @param centurions   list of centurions currently in the game
     * @param TILE_SIZE    tile size used to determine attack range
     * @param canvas       canvas where the game is being rendered
     * @param gc           graphics context used for rendering
     * @return the current game state: either RUNNING or GAME_OVER if Asterix's health reaches zero
     */
    public GameState checkForAttacks(Asterix player, ArrayList<RomanSoldier> romanSoldiers, ArrayList<Centurion> centurions,int TILE_SIZE,Canvas canvas, GraphicsContext gc) {
        long currentTime = System.currentTimeMillis();

        //cooldown 1 sec
        if (currentTime - lastDamageTime >= 1_000) {
            for (RomanSoldier r : romanSoldiers) {
                if (Math.abs(r.getX() - player.getX()) < TILE_SIZE / 4 &&
                        Math.abs(r.getY() - player.getY()) < TILE_SIZE / 4) {
                    player.decreaseHealth();
                    lastDamageTime = currentTime;
                    if (player.getHealth() < 1) {
                        return GameState.GAME_OVER;
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
                        return GameState.GAME_OVER;
                    }
                }
            }
            if(centurions.isEmpty() && romanSoldiers.isEmpty()){
                return GameState.WON;
            }
        }
        return GameState.RUNNING;
    }
    //on pressing T - save inventory

    /**
     * save inventory on current state into InventoryAsterix
     * @param inventory current player's inventory
     */
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
            logger.info("Inventory saved successfully.");
        } catch (IOException e) {
            logger.warning("Error while saving inventory: " + e.getMessage());
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
            logger.info("Inventory loaded successfully.");
        } catch (IOException e) {
            logger.warning("Error while loading inventory: " + e.getMessage());
        }

    }
    public void increaseMana(Asterix player){
        if(mana < manaCapacity){
            this.mana++;
        }
    }
    public void decreaseMana(Asterix player){
        if(player.getMana() > 0){
            player.mana--;
        }
    }
}











