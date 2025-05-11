package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.image.Image;

public class Potion extends Item{
    private Image SHARED_IMAGE =new Image(getClass().getResourceAsStream("/potion.png"));;
    public Potion(double x, double y) {
        super(x,y);
        image = SHARED_IMAGE;
    }

}
