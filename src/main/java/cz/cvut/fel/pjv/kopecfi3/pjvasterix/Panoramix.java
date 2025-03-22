package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Panoramix extends Character {
    private final Image image = new Image(getClass().getResourceAsStream("/druid.png.png"));
    public Panoramix(int x, int y, int health) {
        super(x, y, health);
    }

    public Image getImage() {
        return image;
    }

    public void craftPotion(String potion) {}

    public void interact(){}
}

