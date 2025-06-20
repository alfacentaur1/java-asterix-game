package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import java.util.ArrayList;

public class Panoramix {
    private final double x;
    private final double y;
    private final Image image = new Image(getClass().getResourceAsStream("/druid.png.png"));
    public Panoramix(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Image getImage() {
        return image;
    }

    /**
     * strength potion - i
     * health potion - o
     * attack potion - p
     * if asterix is near enough panoramix, he will check for resources in his inventory
     * if he doesn't have enough resources, he will say "go away"
     * else asterix can choose which potion he would like
     * @param gc graphics context
     * @return true if asterix has enough resources else false
     */
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
        //check if player has enough resources
        if(shroomCounter < 2 || bucketCounter < 2 || carrotCounter < 2 ){
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


    /**
     * returns string of potion which asterix has ordered
     * @param keyPressed keypress near panoramix, choice for potion
     * @param gc graphic context
     * @return String of potion name chose
     */
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

