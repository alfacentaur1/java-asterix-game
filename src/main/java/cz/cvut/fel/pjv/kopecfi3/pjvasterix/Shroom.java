package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;


public class Shroom extends Item{
    private static final Image SHARED_IMAGE = new Image(Shroom.class.getResourceAsStream("/shroom.png"));

    public Shroom(int x, int y) {
        super(x, y);
        image = SHARED_IMAGE;
    }


}
