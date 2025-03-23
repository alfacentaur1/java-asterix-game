package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public abstract class Item {
    protected double x, y;
    protected double mapX, mapY;
    protected Image image;
    public Item(double x, double y) {
        this.x = x;
        this.y = y;
        this.mapX = x;
        this.mapY = y;
    }

    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    //Asterix will collect items and save them into inventory
    public abstract void collect(Asterix asterix);
}
