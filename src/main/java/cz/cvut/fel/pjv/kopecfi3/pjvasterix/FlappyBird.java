package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import java.io.IOException;

public class FlappyBird extends Application {
    private final Image bg = new Image("background.png");
    private final Image image = new Image("bird1.png");
    private final double appWidth = bg.getWidth();
    private final double appHeight = bg.getHeight();

    @Override
    public void start(Stage stage) throws IOException {
        Canvas canvas = new Canvas(bg.getWidth(), bg.getHeight());
        StackPane root = new StackPane(canvas);

        drawBackground(canvas);
        drawItems(canvas);

        Scene scene = new Scene( root,appWidth, appHeight);
        stage.setTitle("Flappy bird!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    private void drawBackground(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(bg, 0, 0);
    }

    private void drawItems(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 100, 280);
    }

    public static void main(String[] args) {
        launch();
    }
}