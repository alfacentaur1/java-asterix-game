package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class WaterBucket extends Item {

    public WaterBucket(int x, int y) {
        super(x, y);
        image = new Image(getClass().getResourceAsStream("/bucket.png"));
    }

}
