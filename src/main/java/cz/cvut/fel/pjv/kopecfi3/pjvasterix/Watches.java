package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Watches extends Thread {
    private boolean runs = true;
    private long startTime;
    GraphicsContext gc;
    private long elapsedTime;

    double seconds= elapsedTime / 1000.0;


    public Watches(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * function to count current time in game - seconds
     */
    public void run(){
        startTime = System.currentTimeMillis();
        while (runs) {
            elapsedTime = System.currentTimeMillis() - startTime;
            seconds = elapsedTime / 1000.0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * function to draw time
     */
    public void drawTime() {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 10));
        gc.fillText("Time spend on level: " + String.format("%.0f", seconds) + "s", 760,538);
    }
    public void stopWatches(){
        runs = false;
    }
}


