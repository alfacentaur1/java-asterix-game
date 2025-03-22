package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Obelix extends Character {
    private Image image;
    public Obelix(int x, int y, int health) {
        super(x, y, health);
        image = new Image(getClass().getResourceAsStream("/obelix.png"));
    }

    public Image getImage() {
        return image;
    }

    public void giveHint() {
    }
}
