package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Carrot extends Item {
    private static final Image SHARED_IMAGE = new Image(Carrot.class.getResourceAsStream("/carrot.png"));
    public Carrot(int x, int y) {
        super(x, y);
        image =SHARED_IMAGE;
    }


}
