package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.io.Serializable;

public class RomanSoldier extends Character implements WarriorStats{
    // Direction ("UP", "DOWN", "LEFT", "RIGHT")
    private String heading;
    private int track_x;
    private int track_y;
    private int pos_x;
    private int pos_y;

    public RomanSoldier(int x, int y, int health, int speed,int track_x, int track_y) {
        super(x, y, health, speed);
        this.track_x = track_x;
        this.track_y = track_y;

    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getTrack_x() {
        return track_x;
    }

    public void setTrack_x(int track_x) {
        this.track_x = track_x;
    }

    public int getTrack_y() {
        return track_y;
    }

    public void setTrack_y(int track_y) {
        this.track_y = track_y;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
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
