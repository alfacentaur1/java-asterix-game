package UnitTests;

import cz.cvut.fel.pjv.kopecfi3.pjvasterix.Asterix;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class AsterixTest {
    private Asterix asterix;
    private int[][] tileMap = {
            {3, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 3, 3},
            {3, 3, 4, 3, 3, 3, 2, 2, 0, 0, 0, 0, 0, 3, 2},
            {2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0},
            {2, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2},
            {2, 2, 1, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 3},
            {2, 2, 1, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 2},
            {2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 3, 4},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
    };
    @BeforeEach
    public void setUp() {
        asterix = new Asterix(380,370,5);

        Image mockImage = mock(Image.class);
        asterix.setPlayerImage(mockImage);
    }
    @Test
    public void testMoveIsAvalaibleToMove() {
        Asterix asterixSpy = Mockito.spy(asterix);

        asterixSpy.move(1,1,tileMap);

        double yAfterMove = asterixSpy.getY();
        double xAfterMove = asterixSpy.getX();

        Assertions.assertEquals(371,yAfterMove);
        Assertions.assertEquals(381,xAfterMove);
    }

    /**
     * Asterix would be out of bounds, so we stop him
     */
    @Test
    public void testMoveIsNotAvalaibleToMove() {
        Asterix asterixSpy = Mockito.spy(asterix);

        asterixSpy.move(1000,1000,tileMap);

        double yAfterMove = asterixSpy.getY();
        double xAfterMove = asterixSpy.getX();

        Assertions.assertEquals(370,yAfterMove);
        Assertions.assertEquals(380,xAfterMove);
    }
}
