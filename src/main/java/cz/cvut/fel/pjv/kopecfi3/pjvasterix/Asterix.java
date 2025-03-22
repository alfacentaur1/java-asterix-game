package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Asterix extends Character {
    private int attackPower = 1;
    private double prevX, prevY;
    private final int tile_size = ViewController.getTILE_SIZE();
    private final int map_width = ViewController.getMapWidth();
    private final int map_height = ViewController.getMapHeight();
    private Image playerImage;
    private int speed = 10;
    private long lastDamageTime = 0;

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

    public double getPrevX() {
        return prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public Asterix(double x, double y, int health) {
        super(10, 10, 10);
        this.playerImage = new Image(getClass().getResourceAsStream("/asterix.png"));
        this.prevX = x;
        this.prevY = y;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

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

    public int getCurrentTile(int[][] tileMap, int x, int y) {
        int tileX = (x + 19) / tile_size;
        int tileY = (y + 23) / tile_size;

        if (tileX >= 0 && tileX < tileMap[0].length && tileY >= 0 && tileY < tileMap.length) {
            return tileMap[tileY][tileX];
        }

        return 0;
    }

    //loop through romans, decrement is for not to go out of bounds
    public void attack(Asterix player,ArrayList<RomanSoldier> romanSoldiers,int TILE_SIZE) {
        int decrement=0;
        for(int i=0;i<romanSoldiers.size();i++) {
            RomanSoldier r = romanSoldiers.get(i);
            if (Math.abs(romanSoldiers.get(i).getX() - player.getX()) < TILE_SIZE / 4 &&
                    Math.abs(r.getY() - player.getY()) < TILE_SIZE / 4){
                r.decreaseHealth();
                if(r.getHealth() <= 0) {
                    romanSoldiers.remove(i-decrement);
                    decrement++;
                }
            }
        }
    }

    public void decreaseHealth() {
        health--;
    }

    public void checkForAttacks(Asterix player, ArrayList<RomanSoldier> romanSoldiers, int TILE_SIZE) {
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
        }
    }
}






