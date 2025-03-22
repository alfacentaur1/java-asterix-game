package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public abstract class Item {
    protected int x, y;
    protected Image image;
    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    //Asterix will collect items and save them into inventory
    public abstract void collect(Asterix asterix);
}
