package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

import java.util.ArrayList;


public class RomanSoldier extends Character {
    private int start_x;
    private int end_x;
    private int start_y;
    private int end_y;
    private static final Image SHARED_IMAGE = new Image(RomanSoldier.class.getResourceAsStream("/roman.png.png"));
    private Image playerImage;
    private String walk_direction; //y or x
    private double speed = 0.3;


    public RomanSoldier(int x, int y ,int start_x, int end_x, int start_y, int end_y, String walk_direction) {
        super(x, y,3);
        this.start_x = start_x;
        this.end_x = end_x;
        this.start_y = start_y;
        this.end_y = end_y;
        this.playerImage = SHARED_IMAGE;
        this.walk_direction = walk_direction;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    /**
     * we walk towards the finish, when we reach it, we switch the final destination to start
     * if we reached the destination we just multiply the speed by -1
     * @throws InterruptedException
     */
    public void move() throws InterruptedException {
        if (this.walk_direction.equals("y")) {
            if (this.y >= this.end_y) {
                this.speed = -Math.abs(this.speed);
            }
            else if (this.y <= this.start_y) {
                this.speed = Math.abs(this.speed);
            }
            this.y += this.speed;
        }
        else if (this.walk_direction.equals("x")) {
            if (this.x >= this.end_x) {
                this.speed = -Math.abs(this.speed);
            }
            else if (this.x <= this.start_x) {
                this.speed = Math.abs(this.speed);
            }
            this.x += this.speed;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth(){
        this.health--;
    }
    //we check for the end of the game - if there are no romans left - return true,else false
    /**
     * check for the end of the game - if there are no romans left - return true,else false
     * @param romanSoldiers current list of romansoldiers in game
     * @return true if there are no romans left else return false
     */
    public static boolean checkForEnd(ArrayList<RomanSoldier> romanSoldiers) {
        int counter = 0;
        for(RomanSoldier romanSoldier : romanSoldiers) {
            counter++;
        }
        if(counter <1){
            return true;
        }
        return false;
    }
}

