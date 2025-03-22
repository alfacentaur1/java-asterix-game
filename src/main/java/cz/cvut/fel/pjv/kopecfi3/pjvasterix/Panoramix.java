package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Panoramix extends Character {
    private final Image image = new Image(getClass().getResourceAsStream("/druid.png.png"));
    public Panoramix(int x, int y, int health) {
        super(x, y, health);
    }

    public Image getImage() {
        return image;
    }

    //strength potion - i
    //health potion - o
    //attack potion - p
    public boolean enoughResources( GraphicsContext gc) {
        double yPos = y - 40;
        ArrayList<Item> items = Inventory.getItems();
        if(items==null){
            return false;
        }
        int carrotCounter = 0;
        int bucketCounter = 0;
        int shroomCounter = 0;

        for (Item item : items) {
            if(item instanceof Carrot){
                carrotCounter++;
            } else if (item instanceof WaterBucket) {
                bucketCounter++;
            }else if (item instanceof Shroom){
                shroomCounter++;
            }
        }
        if(shroomCounter < 2 || bucketCounter < 1 || carrotCounter < 2 ){
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillRoundRect(x, yPos, 220, 60, 10, 10);
            gc.setStroke(javafx.scene.paint.Color.BLACK);
            gc.strokeRoundRect(x, yPos, 220, 60, 10, 10);
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.setFont(new javafx.scene.text.Font(10));
            gc.setFont(Font.font("Times New Roman", 10));
            gc.fillText("Asterix, you don't have enough resources. - pano", x + 10, yPos + 15);
            gc.fillText("Come back later.", x + 10, yPos + 30);
            gc.fillText("Now go away, im busy!", x + 10, yPos + 45);
            return false;
        }

        return true;


    }

    public String craftPotion(KeyCode keyPressed,GraphicsContext gc) {
        double yPos = y - 40;
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRoundRect(x, yPos, 260, 60, 10, 10);
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.strokeRoundRect(x, yPos, 260, 60, 10, 10);
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.setFont(new javafx.scene.text.Font(10));
        gc.setFont(Font.font("Times New Roman", 10));
        if(keyPressed == KeyCode.I){
            gc.fillText("You gained attack potion.", x + 10, yPos + 30);
            return "attack";
        }else if(keyPressed == KeyCode.O){
            gc.fillText("You gained health potion.", x + 10, yPos + 30);
            return "health";
        }else{
            gc.fillText("You gained speed potion.", x + 10, yPos + 30);
            return "speed";
        }

    }
}

