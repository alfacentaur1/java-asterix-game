package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class WaterBucket extends Item {
    private static final Image SHARED_IMAGE = new Image(WaterBucket.class.getResourceAsStream("/bucket.png"));

    public WaterBucket(int x, int y) {
        super(x, y);
        image = SHARED_IMAGE;
    }

}
