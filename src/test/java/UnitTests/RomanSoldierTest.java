package UnitTests;

import cz.cvut.fel.pjv.kopecfi3.pjvasterix.RomanSoldier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class RomanSoldierTest {
    private ArrayList<RomanSoldier> romanSoldiers;

    @BeforeEach
    public void setUp() {
        romanSoldiers = new ArrayList<>();
    }
    //empty list, check for end game
    @Test
    public void checkForEndTestForEndEmptyList(){
        RomanSoldier.checkForEnd(romanSoldiers);

        boolean resultExpectedTrue = RomanSoldier.checkForEnd(romanSoldiers);
        Assertions.assertTrue(resultExpectedTrue);
    }

    @Test

    //if list is not empty, gamestate should be running
    public void checkForEndTestForEndNotEmptyList(){
        RomanSoldier mockedRoman = Mockito.mock(RomanSoldier.class);

        romanSoldiers.add(mockedRoman);
        boolean resultExpectedFalse = RomanSoldier.checkForEnd(romanSoldiers);

        Assertions.assertFalse(resultExpectedFalse);
    }
}
