package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;


public class Villager extends Character {
    private int start_x;
    private int end_x;
    private int start_y;
    private int end_y;
    private Image playerImage;
    private String walk_direction; //y or x
    private double speed = 0.4;


    public Villager(int x, int y, int health, int start_x, int end_x, int start_y, int end_y, String walk_direction) {
        super(x, y, health);
        this.start_x = start_x;
        this.end_x = end_x;
        this.start_y = start_y;
        this.end_y = end_y;

        this.playerImage = new Image(getClass().getResourceAsStream("/fisherman.png"));
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
        // Check the movement direction - if moving along the Y-axis
        if (this.walk_direction.equals("y")) {
            // if the upper movement limit is reached, reverse the direction
            if (this.y >= this.end_y) {
                this.speed = -Math.abs(this.speed);
            }
            // ff the lower movement limit is reached, reverse the direction
            else if (this.y <= this.start_y) {
                this.speed = Math.abs(this.speed);
            }
            // update the Y position
            this.y += this.speed;
        }
        // Check the movement direction - if moving along the X-axis
        else if (this.walk_direction.equals("x")) {
            // If the right movement limit is reached, reverse the direction
            if (this.x >= this.end_x) {
                this.speed = -Math.abs(this.speed);
            }
            // If the left movement limit is reached, reverse the direction
            else if (this.x <= this.start_x) {
                this.speed = Math.abs(this.speed);
            }
            // Update the X position
            this.x += this.speed;
        }
    }

}



