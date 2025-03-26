package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Carrot extends Item {

    public Carrot(int x, int y) {
        super(x, y);
        image = new Image(getClass().getResourceAsStream("/carrot.png"));
    }


}
