package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ViewController extends Application {
    private static final int TILE_SIZE = 64;  // Size of a single tile
    private static final int MAP_WIDTH = 15;  // Number of tiles in width
    private static final int MAP_HEIGHT = 10; // Number of tiles in height
    private static final int SCREEN_WIDTH = 8;  // Visible tiles horizontally
    private static final int SCREEN_HEIGHT = 8; // Visible tiles vertically
    private Image grass;
    private Image wall_vertical;
    private Image asterixattack;
    private Image path;
    private Image wall_horizontal;
    private Image house;
    private Canvas canvas;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private Image water;
    private Image bridgevertical;
    private Image bridgehorizontal;
    ArrayList<Villager> villagers = new ArrayList<Villager>();
    ArrayList<RomanSoldier> romanSoldiers = new ArrayList<RomanSoldier>();
    ArrayList<Item> items = new ArrayList<Item>();


    // Tile map definition (0 = path, 1 = house, 2 = grass, 3 = water, 4 = bridge horizontal)
    private int[][] tileMap = {
            {2, 2, 2, 2, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 2, 2, 2, 1},
            {2, 0, 1, 1, 1, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 1, 0, 0, 1, 0, 2, 0, 3, 0, 0, 3, 0, 3},
            {2, 0, 1, 1, 1, 1, 0, 2, 0, 3, 0, 0, 3, 0, 3},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 3, 3, 3, 3},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 3, 4, 3, 3},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1},
            {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1}

    };
    private final Asterix player = new Asterix(5, 5, 100);
    private final Villager villager1 = new Villager(20, 50, 10, 40, 30, 10, 120, "y");
    private final RomanSoldier roman1 = new RomanSoldier(120, 170, 10, 100, 200, 120, 150, "x");
    private final Obelix obelix = new Obelix(40, 100, 10);
    private final Panoramix panoramix = new Panoramix(100, 100, 10);
    private final Carrot carrot = new Carrot(200, 100);
    private final Shroom shroom = new Shroom(500, 100);
    private final WaterBucket waterBucket = new WaterBucket(400, 100);

    public ViewController() {
        villagers.add(villager1);
        romanSoldiers.add(roman1);
        items.add(carrot);
        items.add(shroom);
        items.add(waterBucket);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static int getTILE_SIZE() {
        return TILE_SIZE;
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static int getMapWidth() {
        return MAP_WIDTH;
    }

    public static int getMapHeight() {
        return MAP_HEIGHT;
    }


    @Override
    public void start(Stage stage) {
        grass = new Image(getClass().getResourceAsStream("/grass.png"));
        wall_vertical = new Image(getClass().getResourceAsStream("/wall-vertical.png"));
        wall_horizontal = new Image(getClass().getResourceAsStream("/wall-horizontal.png"));
        house = new Image(getClass().getResourceAsStream("/House_Blue.png"));
        path = new Image(getClass().getResourceAsStream("/path.png"));
        water = new Image(getClass().getResourceAsStream("/water.png"));
        bridgehorizontal = new Image(getClass().getResourceAsStream("/bridgehorizontal.png"));
        bridgevertical = new Image(getClass().getResourceAsStream("/bridgevertical.png"));
        asterixattack = new Image(getClass().getResourceAsStream("/asterixattack.png"));


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
        drawObelix(gc);
        drawStatusBar(gc);


        //add keys pressed to the map - get code of the key and call the function
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            handleMovement();
            
        });
        //on release delete by code in hashmap
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                redraw(gc);


                if (interactObelix()) {
                    obelix.talk(gc);
                }

                if (interactPanoramix()) {
                    if (panoramix.enoughResources(gc) )
                     {

                        KeyCode keyPressed = null;
                        for (KeyCode key : pressedKeys) {
                            if (key == KeyCode.I) {
                                keyPressed = KeyCode.I;

                                break;
                            }
                            if (key == KeyCode.O) {
                                keyPressed = KeyCode.O;
                                break;
                            }
                            if (key == KeyCode.P) {
                                keyPressed = KeyCode.P;
                                break;
                            }

                        }

                        if (keyPressed != null) {
                            String upgrade = panoramix.craftPotion(keyPressed, gc);
                            switch (upgrade) {
                                case "speed":
                                    System.out.println("Potion: Speed crafted!");
                                    player.setSpeed(player.getSpeed()*2);
                                    break;
                                case "health":
                                    System.out.println("Potion: Health crafted!");
                                    player.setHealth(10);
                                    break;
                                case "attack":
                                    System.out.println("Potion: Attack crafted!");
                                    player.setAttackPower(3);
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

        gameLoop.start();
        stage.show();
        root.requestFocus();
    }


    /**
     * Draws the tile map on the canvas.
     */
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
        gc.drawImage(player.getPlayerImage(), player.getX(), player.getY(), TILE_SIZE/2.2, TILE_SIZE/2.2);
    }
    //redraw the map on every iteration of game loop
    private void redraw(GraphicsContext gc) {
        drawMap(gc);
        drawItems(gc);
        drawPlayer(gc);
        drawStatusBar(gc);


        try {
            villager1.move();
            roman1.move();
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawVillagers(gc);
        drawRomans(gc);
        drawObelix(gc);
        drawPanoramix(gc);

    }

    private void handleMovement() {
        int dx = 0;
        int dy = 0;
        if (pressedKeys.contains(KeyCode.W)) {
            dy = -1* player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.S)) {
            dy = 1* player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.A)) {
            dx = -1* player.getSpeed();
        }
        if (pressedKeys.contains(KeyCode.D)) {
            dx = 1* player.getSpeed();
        }

        player.move(dx, dy, tileMap);
        redraw(canvas.getGraphicsContext2D());

    }

    private void drawVillagers(GraphicsContext gc) {
        for (Villager v : villagers) {
            gc.drawImage(v.getPlayerImage(), v.getX(), v.getY(), TILE_SIZE / 2.2, TILE_SIZE / 2.2);
        }
    }

    private void drawRomans(GraphicsContext gc) {
        for (RomanSoldier r : romanSoldiers) {
            gc.drawImage(r.getPlayerImage(), r.getX(), r.getY(), TILE_SIZE / 1.8, TILE_SIZE / 1.8);
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
        if (Math.abs(player.getX() - obelix.getX()) < TILE_SIZE/3.5 &&
                Math.abs(player.getY() - obelix.getY()) < TILE_SIZE/3.5) {
            return true;
        }
        return false;

    }
    private boolean interactPanoramix() {
        if (Math.abs(player.getX() - panoramix.getX()) < TILE_SIZE/3.5 &&
                Math.abs(player.getY() - panoramix.getY()) < TILE_SIZE/3.5) {
            return true;
        }
        return false;

    }
    private void drawStatusBar(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double x = width - 150;
        double y = height - 60;


        gc.setFill(Color.LIGHTGRAY);
        gc.fillRoundRect(x, y, 140, 50, 10, 10);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, 140, 50, 10, 10);


        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 12));
        gc.fillText("Health: " + player.getHealth(), x + 10, y + 15);
        gc.fillText("Speed: " + player.getSpeed(), x + 10, y + 30);
        gc.fillText("Attack: " + player.getAttackPower(), x + 10, y + 45);
    }

}






