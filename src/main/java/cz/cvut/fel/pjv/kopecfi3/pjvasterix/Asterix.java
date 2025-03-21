package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Asterix extends Character {
    private int attackPower;
    private double prevX, prevY;
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private final int tile_size = ViewController.getTILE_SIZE();
    private final int screen_width = ViewController.getScreenWidth();
    private final int screen_height = ViewController.getScreenHeight();
    private final int map_width = ViewController.getMapWidth();
    private final int map_height = ViewController.getMapHeight();
    private Image playerImage;

    public int getAttackPower() {
        return attackPower;
    }


    public ArrayList<Item> getInventory() {
        return inventory;
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

    public Asterix(double x, double y, int health) {
        super(10, 10, health);
        this.playerImage = new Image(getClass().getResourceAsStream("/asterix.png"));
        this.prevX = x;
        this.prevY = y;
    }

    public int getStrength() {
        return attackPower;
    }

    public void setStrength(int attackPower) {
        this.attackPower = attackPower;
    }

    public void move(int dx, int dy, int[][] tileMap) {
        int new_x = (int)x + dx;
        int new_y = (int)y + dy;
        if(getCurrentTile(tileMap,(int)new_x-5,(int)new_y-5) != 1 && getCurrentTile(tileMap,new_x-5,new_y-5) != 3 && getCurrentTile(tileMap,new_x-5,new_y-5) != 4 )
        {
            if(new_x >= -5 && new_x < map_width*tile_size && new_y >= -5 && new_y < map_height*tile_size){
                x = new_x;
                y = new_y;
            }
        }
    }

    public int getCurrentTile(int[][] tileMap, int x, int y) {
        int tileX = (x+38) / tile_size;
        int tileY = (y+46) / tile_size;

        if (tileX >= 0 && tileX < tileMap[0].length && tileY >= 0 && tileY < tileMap.length) {
            System.out.println("current tile: " + tileMap[tileY][tileX]);
            return tileMap[tileY][tileX];
        }

        return -1;
    }


        public void attack() {

        }


        //Depends on which potion will he drink
        public void drinkPotion () {

        }


    }

