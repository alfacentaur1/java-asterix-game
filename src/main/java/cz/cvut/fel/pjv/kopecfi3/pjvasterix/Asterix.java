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
    private int speed = 1;

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

    public Asterix(double x, double y,int health) {
        super(10, 10, 10);
        this.playerImage = new Image(getClass().getResourceAsStream("/asterix.png"));
        this.prevX = x;
        this.prevY = y;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void move(int dx, int dy, int[][] tileMap) {
        int new_x = (int)x + dx;
        int new_y = (int)y + dy;
        if(getCurrentTile(tileMap,(int)new_x-5,(int)new_y-5) != 1 && getCurrentTile(tileMap,new_x-5,new_y-5) != 3 )
        {
            if(new_x >= -5 && new_x < map_width*tile_size && new_y >= -5 && new_y < map_height*tile_size){
                x = new_x;
                y = new_y;
            }
        }
    }

    public int getCurrentTile(int[][] tileMap, int x, int y) {
        int tileX = (x+19) / tile_size;
        int tileY = (y+23) / tile_size;

        if (tileX >= 0 && tileX < tileMap[0].length && tileY >= 0 && tileY < tileMap.length) {
            return tileMap[tileY][tileX];
        }

        return 0;
    }


        public void attack() {

        }




    }

