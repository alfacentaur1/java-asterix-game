package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.awt.*;

public class Obelix extends Character {
    private Image image;
    private String text1 = "Hello Asterix, ";
    private String text2 = "the water bucket should be somewhere near the water,";
    private String text3 = "but I have forgotten, where I put it.";
    public Obelix(int x, int y, int health) {
        super(x, y, health);
        image = new Image(getClass().getResourceAsStream("/obelix.png"));
    }

    public Image getImage() {
        return image;
    }

    public void talk(GraphicsContext gc) {
        double yPos = y - 40;

        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRoundRect(x, yPos, 260, 60, 10, 10);
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.strokeRoundRect(x, yPos, 260, 60, 10, 10);

        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.setFont(new javafx.scene.text.Font(10));
        gc.setFont(Font.font("Times New Roman", 10));

        gc.fillText(text1, x + 10, yPos + 15);
        gc.fillText(text2, x + 10, yPos + 30);
        gc.fillText(text3, x + 10, yPos + 45);
    }
    }


