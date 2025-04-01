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


}
