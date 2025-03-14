package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.io.Serializable;

public class RomanSoldier extends Character implements WarriorStats{
    // Direction ("UP", "DOWN", "LEFT", "RIGHT")
    private String heading;

    public RomanSoldier(int x, int y, int health, int speed) {
        super(x, y, health, speed);

    }

    @Override
    public void move(int x, int y) {

    }

    public void turn(String newHeading) {
        this.heading = newHeading;
    }

    public String getHeading() {
        return heading;
    }

    @Override
    public void attack() {

    }
}
