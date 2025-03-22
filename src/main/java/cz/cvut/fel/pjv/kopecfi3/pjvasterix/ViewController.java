package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ViewController extends Application {
    private static final int TILE_SIZE = 64;  // Size of a single tile
    private static final int MAP_WIDTH = 25;  // Number of tiles in width
    private static final int MAP_HEIGHT = 25; // Number of tiles in height
    private static final int SCREEN_WIDTH = 8;  // Visible tiles horizontally
    private static final int SCREEN_HEIGHT = 8; // Visible tiles vertically
    private Image grass;
    private Image wall_vertical;
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
            {2, 2, 2, 2, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 3, 3, 3, 0, 0, 0, 3, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 2, 2, 2, 1, 1, 0, 2, 2, 2, 3, 3, 0, 3, 0, 3, 3, 0, 2, 2},
    {2, 0, 1, 1, 1, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, 3, 2, 2},
    {2, 0, 1, 0, 0, 1, 0, 2, 0, 3, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 3, 0, 0, 0, 3, 3, 2, 2},
    {2, 0, 1, 1, 1, 1, 0, 2, 0, 3, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 3, 3, 0, 0, 0, 3, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3, 2, 0, 0, 0, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 3, 4, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3, 2, 0, 0, 0, 2, 2},
    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 4, 4, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2},
    {2, 0, 0, 0, 0, 2, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 0, 2, 2, 2, 2, 3, 3, 0, 3, 3, 0, 3, 2, 2}

};
    private final Asterix player = new Asterix(5, 5, 100);
    private final Villager villager1 = new Villager(20,50,10,40,30,10,120,"y");
    private final RomanSoldier roman1 = new RomanSoldier(120,170,10,100,200,120,150,"x");
    private final Obelix obelix = new Obelix(40,100,10);
    private final Panoramix panoramix = new Panoramix(100,100,10);
    private final Carrot carrot = new Carrot(200,100);
    private final Shroom shroom = new Shroom(500,100);
    private final WaterBucket waterBucket = new WaterBucket(400,100);

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
        bridgevertical = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bridgevertical.png")));

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
                redraw(canvas.getGraphicsContext2D());
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
                }
                else if (tileType == 2) {
                    gc.drawImage(grass, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else if (tileType == 3) {
                    gc.drawImage(water, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else {
                    gc.drawImage(bridgehorizontal, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
    private Image getTileImage(int tileType) {
        switch (tileType) {
            case 0: return path;
            case 1: return house;
            case 2: return grass;
            case 3: return wall_vertical;
            case 4: return wall_horizontal;
            default: return grass;
        }
    }

    private void drawPlayer(GraphicsContext gc) {
        gc.drawImage(player.getPlayerImage(), player.getX(), player.getY(), TILE_SIZE, TILE_SIZE);
    }

    private void redraw(GraphicsContext gc) {
        drawMap(gc);
        drawItems(gc);
        drawPlayer(gc);


        try{
            villager1.move();
            roman1.move();
        }catch(Exception e){
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
            dy = -1;
        }
        if (pressedKeys.contains(KeyCode.S)) {
            dy = 1;
        }
        if (pressedKeys.contains(KeyCode.A)) {
            dx = -1;
        }
        if (pressedKeys.contains(KeyCode.D)) {
            dx = 1;
        }

        player.move(dx, dy, tileMap);
        redraw(canvas.getGraphicsContext2D());

    }
    private void drawVillagers(GraphicsContext gc) {
        for(Villager v : villagers) {
            gc.drawImage(v.getPlayerImage(), v.getX(), v.getY(), TILE_SIZE/2.2, TILE_SIZE/2.2);
        }
    }

    private void drawRomans(GraphicsContext gc) {
        for (RomanSoldier r : romanSoldiers) {
            gc.drawImage(r.getPlayerImage(), r.getX(), r.getY(), TILE_SIZE / 1.8, TILE_SIZE / 1.8);
        }
    }

    private void drawObelix(GraphicsContext gc){
            gc.drawImage(obelix.getImage(), obelix.getX(), obelix.getY(), TILE_SIZE/1.2, TILE_SIZE/1.2);
        }
    private void drawPanoramix(GraphicsContext gc){
        gc.drawImage(panoramix.getImage(), panoramix.getX(), panoramix.getY(), TILE_SIZE/1.2, TILE_SIZE/1.2);
    }

    private void drawItems(GraphicsContext gc) {
        for (Item item : items) {
            gc.drawImage(item.getImage(), item.getX(), item.getY(), TILE_SIZE/1.2, TILE_SIZE/1.2);

        }
    }

    }




