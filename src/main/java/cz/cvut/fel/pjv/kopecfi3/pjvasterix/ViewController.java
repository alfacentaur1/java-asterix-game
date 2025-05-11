package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;


public class ViewController extends Application {
    //initialize
    private static final int TILE_SIZE = 64;  // Size of a single tile
    private static final int MAP_WIDTH = 15;  // Number of tiles in width
    private static final int MAP_HEIGHT = 10; // Number of tiles in height
    private static final int SCREEN_WIDTH = 8;  // Visible tiles horizontally
    private static final int SCREEN_HEIGHT = 8; // Visible tiles vertically
    private Image grass;
    private Image asterixattack;
    private Image path;
    private Image house;
    private Canvas canvas;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private Image water;
    private Image bridgehorizontal;
    ArrayList<Villager> villagers = new ArrayList<Villager>();
    ArrayList<RomanSoldier> romanSoldiers = new ArrayList<RomanSoldier>();
    ArrayList<Item> items = new ArrayList<Item>();
    Inventory inventory = new Inventory();
    ArrayList<Centurion> centurions = new ArrayList<Centurion>();
    private Asterix player = null;
    private Obelix obelix = null;
    private Panoramix panoramix = null;
    private GameState gameState = GameState.RUNNING;
    private boolean inventoryVisible = false;
    private Watches watches;
    private MediaPlayer mediaPlayerSword;
    private boolean isSwordPlaying = false;
    private static final Logger logger = Logger.getLogger(ViewController.class.getName());


    // Tile map definition (0 = path, 1 = house, 2 = grass, 3 = water, 4 = bridge horizontal)
    FileLoader fileLoader = new FileLoader();
    //private int[][] tileMap = fileLoader.loadMap("src/main/resources/map2.txt");
    private int[][] tileMap;
    EntityLoader entityLoader = new EntityLoader();
    //private ArrayList<Object> allInstances = entityLoader.loadAllMapEntities("src/main/resources/entitiesmap2.txt");
    private ArrayList<Object> allInstances;


    public ViewController(String map, String instances, String loadInventory) {
        //add instances do their lists
        allInstances = entityLoader.loadAllMapEntities("src/main/resources/" + instances);
        tileMap = fileLoader.loadMap("src/main/resources/" + map);


        for (Object o : allInstances) {
            if (o instanceof Villager) {
                villagers.add((Villager) o);
            }
            if (o instanceof RomanSoldier) {
                romanSoldiers.add((RomanSoldier) o);
            }
            if (o instanceof Item) {
                items.add((Item) o);
            }
            if (o instanceof Centurion) {
                centurions.add((Centurion) o);
            }
            if (o instanceof Asterix) {
                player = (Asterix) o;
            }
            if (o instanceof Obelix) {
                obelix = (Obelix) o;
            }
            if (o instanceof Panoramix) {
                panoramix = (Panoramix) o;
            }

        }
        //if menu option would be load - load saved inventory
        if (loadInventory.equals("Yes")) {
            player.loadInventory(inventory);
            logger.info("Inventory succesfully loaded.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public static int getMapWidth() {
        return MAP_WIDTH;
    }

    public static int getMapHeight() {
        return MAP_HEIGHT;
    }

    /**
     * start function
     * @param stage current stage to start gamelooping
     */
    @Override
    public void start(Stage stage) {
        String path_sword = getClass().getResource("/sword.mp3").toExternalForm();
        Media media_sword = new Media(path_sword);
        mediaPlayerSword = new MediaPlayer(media_sword);


        grass = new Image(getClass().getResourceAsStream("/grass.png"));
        house = new Image(getClass().getResourceAsStream("/House_Blue.png"));
        path = new Image(getClass().getResourceAsStream("/path.png"));
        water = new Image(getClass().getResourceAsStream("/water.png"));
        bridgehorizontal = new Image(getClass().getResourceAsStream("/bridgehorizontal.png"));
        asterixattack = new Image(getClass().getResourceAsStream("/asterixattack.png.png"));
        Image asterix = new Image(getClass().getResourceAsStream("/asterix.png"));


        Pane root = new Pane();
        canvas = new Canvas(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        drawMap(gc);

        stage.setScene(scene);
        stage.setTitle("Asterix RPG");
        drawItems(gc);
        drawPlayer(gc);
        drawVillagers(gc);
        drawRomans(gc);
        drawCenturions(gc);
        drawObelix(gc);
        drawStatusBar(gc);
        watches = new Watches(gc);
        watches.start();
        watches.drawTime();


        //add keys pressed to the map - get code of the key and call the function
        //on key presses handle players movement
        //while pressed Q - show inventory
        //press C to clean whole inventory
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            handleMovement();
            if (pressedKeys.contains(KeyCode.T)) {
                player.saveInventory(inventory);
                logger.info("Inventory succesfully saved.");
            }
            if (pressedKeys.contains(KeyCode.Q)) {
                inventoryVisible = true;
            }
            if (!(pressedKeys.contains(KeyCode.Q))) {
                inventoryVisible = false;
            }
            if (event.getCode() == KeyCode.C) {
                inventory = new Inventory();
            }


        });
        Timeline manaRegeneration = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (player.getMana() < 5) {
                player.increaseMana(player);
            }
        }));
        manaRegeneration.setCycleCount(Timeline.INDEFINITE);
        manaRegeneration.play();
        //listen to mouse buttons - attack/collect
        //also on attack change asterix's picture and leave the duration on 0.5 s
        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (player.getMana() > 0) {
                    items = player.attack(player, romanSoldiers, centurions, TILE_SIZE, items);
                    player.setPlayerImage(asterixattack);
                    if (!isSwordPlaying) {
                        mediaPlayerSword.stop();
                        mediaPlayerSword.play();
                        isSwordPlaying = true;

                        Timeline resetSwordSound = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                            isSwordPlaying = false;
                        }));
                        resetSwordSound.setCycleCount(1);
                        resetSwordSound.play();
                    }
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        player.setPlayerImage(asterix);
                    }));
                    timeline.setCycleCount(1); //only once
                    timeline.play();
                }

            }
            if (event.getButton() == MouseButton.PRIMARY) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                //check if player can collect the item
                //check if inventory is not full and add item
                Item itemToAdd = inventory.searchItems(items, mouseX, mouseY, TILE_SIZE, player,inventory);
                if (itemToAdd != null) {
                    if (inventory.getSize() < 6 || inventory == null) {
                        if (itemToAdd instanceof Potion) {
                            player.setAttackPower(player.getAttackPower() * 2);

                        }
                        if (!(itemToAdd instanceof Potion)) {
                            inventory.addItem(itemToAdd);
                            logger.info("Succesfully added to inventory.");
                        }
                    }
                }


            }
        });
        //on release delete by code in hashmap
        //asterix can walk all directions not only left,right,up, down
        scene.setOnKeyReleased(e -> {
                    pressedKeys.remove(e.getCode());
                    if (!pressedKeys.contains(KeyCode.Q)) {
                        inventoryVisible = false;
                    }
                }
        );


        //on end game unfocus scene key inputs - redirect them to null
        /**
         * main loop
         */
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameState = player.checkForAttacks(player, romanSoldiers, centurions, TILE_SIZE, canvas, gc);
                if (gameState == gameState.GAME_OVER) {

                    GraphicsContext gc1 = canvas.getGraphicsContext2D();
                    redraw(gc1, inventoryVisible, inventory);
                    String message = "GAME OVER :(";
                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font(50));
                    gc.fillText(message, canvas.getWidth() / 2 - gc.getFont().getSize() * message.length() / 4, canvas.getHeight() / 2);
                    this.stop();
                    scene.setOnKeyPressed(null);
                    scene.setOnKeyReleased(null);
                    watches.stopWatches();
                    return;
                } else if (gameState == gameState.WON) {
                    GraphicsContext gc1 = canvas.getGraphicsContext2D();
                    redraw(gc1, inventoryVisible, inventory);
                    String message = "YOU WON :)";
                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font(50));
                    gc.fillText(message, canvas.getWidth() / 2 - gc.getFont().getSize() * message.length() / 4, canvas.getHeight() / 2);
                    this.stop();
                    scene.setOnKeyPressed(null);
                    scene.setOnKeyReleased(null);
                    watches.stopWatches();
                    return;
                } else if (romanSoldiers.isEmpty() && centurions.isEmpty()) {
                    GraphicsContext gc1 = canvas.getGraphicsContext2D();
                    redraw(gc1, inventoryVisible, inventory);
                    String message = "YOU WON :)";
                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font(50));
                    gc.fillText(message, canvas.getWidth() / 2 - gc.getFont().getSize() * message.length() / 4, canvas.getHeight() / 2);
                    this.stop();
                    scene.setOnKeyPressed(null);
                    scene.setOnKeyReleased(null);
                    watches.stopWatches();
                    return;
                }
                if (gameState != gameState.RUNNING) {
                    this.stop();
                    scene.setOnKeyPressed(null);
                    scene.setOnKeyReleased(null);
                    watches.stopWatches();
                    return;
                }
                GraphicsContext gc = canvas.getGraphicsContext2D();
                redraw(gc, inventoryVisible, inventory);

                //if some soldier or centurion is near asterix, decrease health

                if (RomanSoldier.checkForEnd(romanSoldiers) && Centurion.checkForEnd(centurions)) {
                    gameState = GameState.GAME_OVER;
                }
                if (!player.isAlive()) {
                    gameState = GameState.GAME_OVER;

                }

                //if asterix is near obelix, interact
                if (interactObelix()) {
                    obelix.talk(gc,player);
                }

                //if asterix is near panoramix, interact
                if (interactPanoramix()) {
                    if (panoramix.enoughResources(gc)) {

                        KeyCode keyPressed = null;
                        for (KeyCode key : pressedKeys) {
                            if (key == KeyCode.I) {
                                keyPressed = KeyCode.I;

                                break;
                            } else if (key == KeyCode.O) {
                                keyPressed = KeyCode.O;
                                break;
                            } else if (key == KeyCode.P) {
                                keyPressed = KeyCode.P;
                                break;
                            }

                        }
                        //clear inventory and give asterix the wanted powerup
                        if (keyPressed != null) {
                            String upgrade = panoramix.craftPotion(keyPressed, gc);
                            switch (upgrade) {
                                case "speed":
                                    inventory = new Inventory();
                                    logger.info("Potion: Speed crafted!");
                                    player.setSpeed(player.getSpeed() * 2);
                                    break;
                                case "health":
                                    inventory = new Inventory();
                                    logger.info("Potion: Health crafted!");
                                    player.setHealth(10);
                                    break;
                                case "attack":
                                    inventory = new Inventory();
                                    logger.info("Potion: Attack crafted!");
                                    if(player.getAttackPower() <= 3){
                                        player.setAttackPower(3);
                                    }

                                    break;
                            }
                        }

                    } else {
                        //not enough resources
                        double x = panoramix.getX();
                        double yPos = panoramix.getY() - 40;
                        gc.setFill(javafx.scene.paint.Color.WHITE);
                        gc.fillRoundRect(x, yPos, 220, 60, 10, 10);
                        gc.setStroke(javafx.scene.paint.Color.BLACK);
                        gc.strokeRoundRect(x, yPos, 220, 60, 10, 10);
                        gc.setFill(javafx.scene.paint.Color.BLACK);
                        gc.setFont(Font.font("Times New Roman", 10));
                        gc.fillText("Asterix, you don't have enough resources.", x + 10, yPos + 15);
                        gc.fillText("Come back later.", x + 10, yPos + 30);
                        gc.fillText("Now go away, I'm busy!", x + 10, yPos + 45);
                    }
                }
            }

        };
        drawItems(gc);

        gameLoop.start();
        stage.show();
        root.requestFocus();

    }



    //goes through the tilemap definition and loops, based on number input draws desired tile
    private void drawMap(GraphicsContext gc) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                int tileType = tileMap[y][x];

                //draws image - mutliplication by tile size - indexes in matrice, not on map
                if (tileType == 0) {
                    gc.drawImage(path, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tileType == 1) {
                    gc.drawImage(house, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tileType == 2) {
                    gc.drawImage(grass, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tileType == 3) {
                    gc.drawImage(water, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    gc.drawImage(bridgehorizontal, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }


    private void drawPlayer(GraphicsContext gc) {
        gc.drawImage(player.getPlayerImage(), player.getX(), player.getY(), TILE_SIZE / 2.2, TILE_SIZE / 2.2);
    }

    //redraw the map on every iteration of game loop
    private void redraw(GraphicsContext gc, Boolean visibleInventory, Inventory inventory) {
        drawMap(gc);
        drawItems(gc);
        watches.drawTime();

        try {
            for (Villager v : villagers) {
                v.move();
            }
            for (RomanSoldier r : romanSoldiers) {
                r.move();
            }
            for (Centurion c : centurions) {
                c.move();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawRomans(gc);
        drawCenturions(gc);
        drawObelix(gc);
        drawPanoramix(gc);
        drawVillagers(gc);
        drawPlayer(gc);
        drawStatusBar(gc);
        drawInventory(gc, visibleInventory, inventory);

    }

    //move asterix's coord based on key and speed - based on hashmap
    private void handleMovement() {
        int dx = 0;
        int dy = 0;
        if (pressedKeys.contains(KeyCode.W)) {
            dy = -1 * player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.S)) {
            dy = 1 * player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.A)) {
            dx = -1 * player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.D)) {
            dx = 1 * player.getSpeed();
        }

        player.move(dx, dy, tileMap);
        redraw(canvas.getGraphicsContext2D(), inventoryVisible, inventory);

    }

    private void drawVillagers(GraphicsContext gc) {
        for (Villager v : villagers) {
            gc.drawImage(v.getPlayerImage(), v.getX(), v.getY(), TILE_SIZE / 2.2, TILE_SIZE / 2.2);
        }
    }

    //draw romans healthbar
    private void drawRomans(GraphicsContext gc) {
        for (RomanSoldier r : romanSoldiers) {
            gc.drawImage(r.getPlayerImage(), r.getX(), r.getY(), TILE_SIZE / 1.8, TILE_SIZE / 1.8);

            //specify the rect
            double healthBarWidth = 20;
            double healthBarHeight = 5;
            double healthPercentage = (double) r.getHealth() / 3;
            double currentHealthWidth = healthBarWidth * (healthPercentage);

            //coords
            double healthBarX = r.getX() + 8;
            double healthBarY = r.getY() - 6;

            //fill the rect based on curr health, health percentage
            gc.setFill(Color.RED);
            gc.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

            gc.setFill(Color.GREEN);
            gc.fillRect(healthBarX, healthBarY, currentHealthWidth, healthBarHeight);

        }
    }

    private void drawCenturions(GraphicsContext gc) {
        for (Centurion r : centurions) {
            gc.drawImage(r.getPlayerImage(), r.getX(), r.getY(), TILE_SIZE / 1.8, TILE_SIZE / 1.8);

            double healthBarWidth = 20;
            double healthBarHeight = 5;
            double healthPercentage = (double) r.getHealth() / 5;
            double currentHealthWidth = healthBarWidth * (healthPercentage);


            double healthBarX = r.getX() + 8;
            double healthBarY = r.getY() - 6;

            gc.setFill(Color.RED);
            gc.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

            gc.setFill(Color.GREEN);
            gc.fillRect(healthBarX, healthBarY, currentHealthWidth, healthBarHeight);

        }
    }

    private void drawObelix(GraphicsContext gc) {
        gc.drawImage(obelix.getImage(), obelix.getX(), obelix.getY(), TILE_SIZE / 1.2, TILE_SIZE / 1.2);
    }

    private void drawPanoramix(GraphicsContext gc) {
        gc.drawImage(panoramix.getImage(), panoramix.getX(), panoramix.getY(), TILE_SIZE / 1.2, TILE_SIZE / 1.2);
    }

    private void drawItems(GraphicsContext gc) {
        for (Item item : items) {
            gc.drawImage(item.getImage(), item.getX(), item.getY(), TILE_SIZE / 1.2, TILE_SIZE / 1.2);
        }
    }

    // If obelix position is closer than one tile, start the conversation
    private boolean interactObelix() {
        if (Math.abs(player.getX() - obelix.getX()) < TILE_SIZE / 3.5 &&
                Math.abs(player.getY() - obelix.getY()) < TILE_SIZE / 3.5) {
            return true;
        }
        return false;

    }

    private boolean interactPanoramix() {
        if (Math.abs(player.getX() - panoramix.getX()) < TILE_SIZE / 3.5 &&
                Math.abs(player.getY() - panoramix.getY()) < TILE_SIZE / 3.5) {
            return true;
        }
        return false;

    }

    //same health bar as in roman, get the left down corner coords
    private void drawStatusBar(GraphicsContext gc) {
        double statusBarX = canvas.getWidth() - 200;
        double statusBarY = canvas.getHeight() - 90;

        double healthBarWidth = 130;
        double healthBarHeight = 10;
        double healthPercentage = (double) player.getHealth() * 10 / 100;
        double currentHealthWidth = healthBarWidth * healthPercentage;

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(statusBarX, statusBarY - 10, 180, 130);

        // health
        gc.setFill(Color.RED);
        gc.fillRect(statusBarX + 40, statusBarY, healthBarWidth, healthBarHeight);
        gc.setFill(Color.GREEN);
        gc.fillRect(statusBarX + 40, statusBarY, currentHealthWidth, healthBarHeight);

        // speed, strenght
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(12));
        gc.fillText("Speed: " + player.getSpeed(), statusBarX + 40, statusBarY + 25);
        gc.fillText("Strength: " + player.getAttackPower(), statusBarX + 40, statusBarY + 40);

        // asterix image
        Image asterix = new Image(getClass().getResourceAsStream("/asterix.png"));
        gc.drawImage(asterix, statusBarX - 5, statusBarY + 10, 50, 50);

        if(player.hasBoots()){
            gc.fillText("Boots: yes", statusBarX + 40, statusBarY + 85);
        } else {
            gc.fillText("Boots: no", statusBarX + 40, statusBarY + 85);
        }

        // Mana
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(12));
        gc.fillText("Mana", statusBarX + 40, statusBarY + 54);
        int mana = player.getMana();
        int manaSize = 10; //px of one brick
        double manaStartX = statusBarX + 40; //start y axis
        double manaStartY = statusBarY + 60; //start x axis



        gc.setFill(Color.DARKBLUE);
        for (int i = 0; i < mana; i++) {
            gc.fillRect(manaStartX + (i * (manaSize + 2)), manaStartY, manaSize, manaSize);
        }
    }

    /**
     * function to show inventory
     * @param gc graphic context
     * @param inventoryVisible if the key is pressed to make inventory visible - Q
     * @param inventory current players inventory
     */
    public void drawInventory(GraphicsContext gc, Boolean inventoryVisible, Inventory inventory) {
        if (inventoryVisible) {
            double inventoryWidth = 500;
            double inventoryHeight = 400;

            double inventoryX = (canvas.getWidth() - inventoryWidth) / 2;
            double inventoryY = (canvas.getHeight() - inventoryHeight) / 2;

            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(inventoryX, inventoryY, inventoryWidth, inventoryHeight);


            double slotSize = Math.min(inventoryWidth / 3, inventoryHeight / 2);
            int cols = 3;
            int rows = 2;


            int index = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    double slotX = inventoryX + (col * slotSize);
                    double slotY = inventoryY + (row * slotSize);

                    if (index < inventory.getSize()) {
                        Item item = inventory.getItem(index);
                        if (item.getImage() != null && item instanceof Shroom) {
                            Image shroomy = new Image(getClass().getResourceAsStream("/mushroom.png"));
                            gc.drawImage(shroomy, slotX, slotY, slotSize, slotSize);
                        }
                        else if(item.getImage() != null && !(item instanceof Shroom)){
                            gc.drawImage(item.getImage(), slotX, slotY, slotSize, slotSize);
                        }
                        else {
                            gc.setFill(Color.GRAY);
                            gc.fillRect(slotX, slotY, slotSize, slotSize);
                        }
                    } else {
                        gc.setFill(Color.GRAY);
                        gc.fillRect(slotX, slotY, slotSize, slotSize);
                    }
                    index++;
                }
            }
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(14));
            gc.fillText("For cleaning whole inventory, press C", inventoryWidth-130 , inventoryY+inventoryHeight-30);
        }
    }
}
