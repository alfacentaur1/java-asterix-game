package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Potion extends Item{

    public Potion(double x, double y) {
        super(x,y);
        image = new Image(getClass().getResourceAsStream("/potion.png"));
    }

    @Override
    public void collect(Asterix asterix) {

    }
}
