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


    public int getStart_x() {
        return start_x;
    }

    public int getEnd_x() {
        return end_x;
    }

    public int getStart_y() {
        return start_y;
    }

    public int getEnd_y() {
        return end_y;
    }

    public String getWalk_direction() {
        return walk_direction;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public void setStart_x(int start_x) {
        this.start_x = start_x;
    }

    public void setEnd_x(int end_x) {
        this.end_x = end_x;
    }

    public void setStart_y(int start_y) {
        this.start_y = start_y;
    }

    public void setEnd_y(int end_y) {
        this.end_y = end_y;
    }

    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public void setWalk_direction(String walk_direction) {
        this.walk_direction = walk_direction;
    }


    //we walk towards the finish, when we reach it, we switch the final destination to start
    //if we reached the destination we just mulitply the speed by -1

    public void move() throws InterruptedException {

        if (this.walk_direction.equals("y")) {

            if (this.y >= this.end_y) {
                this.speed = -(this.speed);
            }
            else if (this.y <= this.start_y) {
                this.speed = -(this.speed);
            }

            this.y += this.speed;
        }
        else if (this.walk_direction.equals("x")) {

            if (this.x >= this.end_x) {
                this.speed = -(this.speed);
            }

            else if (this.x <= this.start_x) {
                this.speed = (this.speed);
            }

            this.x += this.speed;
        }

    }
}


