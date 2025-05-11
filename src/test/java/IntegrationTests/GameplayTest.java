package IntegrationTests;

import cz.cvut.fel.pjv.kopecfi3.pjvasterix.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.text.View;
import java.util.ArrayList;

public class GameplayTest {
    private Inventory inventory;
    private Asterix asterix;
    private ArrayList<Item> items;
    private Panoramix panoramix = new Panoramix(1,1);
    Carrot carrot;
    WaterBucket waterBucket;
    Shroom shroom;
    private ArrayList<RomanSoldier> romanSoldiers;
    private ArrayList<Centurion> centurions;
    private Centurion centurion;
    private RomanSoldier roman;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        items = new ArrayList<>();
        carrot = Mockito.mock(Carrot.class);
        shroom = Mockito.mock(Shroom.class);
        waterBucket = Mockito.mock(WaterBucket.class);
        Asterix asterixOriginal = new Asterix(5,5,10);
        asterix = Mockito.spy(asterixOriginal);

        //setup items on map
        items.add(carrot);
        items.add(shroom);
        items.add(waterBucket);

        //set up enemies on map
        centurion = new Centurion(100, 100, 100, 150, 100, 150, "y");
        centurions = new ArrayList<>();
        centurions.add(centurion);
        roman = new RomanSoldier(150, 150, 150, 180, 150, 180, "y");
        romanSoldiers = new ArrayList<>();
        romanSoldiers.add(roman);
        roman.setHealth(1);



    }

    @Test
    public void testPotionInteraction() {
        int tileSize = 64;

        //check item positions
        Mockito.when(carrot.getX()).thenReturn(30.0);
        Mockito.when(carrot.getY()).thenReturn(30.0);

        Mockito.when(shroom.getX()).thenReturn(100.0);
        Mockito.when(shroom.getY()).thenReturn(30.0);

        Mockito.when(waterBucket.getX()).thenReturn(30.0);
        Mockito.when(waterBucket.getY()).thenReturn(100.0);

        // 1) Player will add carrot (expecting double click)
        Mockito.when(asterix.getX()).thenReturn(30.0);
        Mockito.when(asterix.getY()).thenReturn(30.0);
        Item itemToAdd = inventory.searchItems(items, 30, 30, tileSize, asterix, inventory);
        inventory.addItem(itemToAdd);
        inventory.addItem(itemToAdd);

        //check the size, and if it contains carrots
        Assertions.assertEquals(2, inventory.getSize());
        Assertions.assertTrue(inventory.getItems().contains(carrot));

        // 2) Player will collect shroom (expecting double click)
        Mockito.when(asterix.getX()).thenReturn(100.0);
        Mockito.when(asterix.getY()).thenReturn(30.0);
        itemToAdd = inventory.searchItems(items, 100, 30, tileSize, asterix, inventory);
        inventory.addItem(itemToAdd);
        inventory.addItem(itemToAdd);

        //check the size, and if it contains shrooms
        Assertions.assertEquals(4, inventory.getSize());
        Assertions.assertTrue(inventory.getItems().contains(shroom));

        // 3) Player will collect waterBucket (expecting double click)
        Mockito.when(asterix.getX()).thenReturn(30.0);
        Mockito.when(asterix.getY()).thenReturn(100.0);
        itemToAdd = inventory.searchItems(items, 30, 100, tileSize, asterix, inventory);
        inventory.addItem(itemToAdd);
        inventory.addItem(itemToAdd);

        //check the size, and if it contains carrots
        Assertions.assertEquals(6, inventory.getSize());
        Assertions.assertTrue(inventory.getItems().contains(waterBucket));

        // 4) now check the panoramix interaction - asterix will buy potion
        GraphicsContext gc = Mockito.mock(GraphicsContext.class);
        boolean hasEnough = panoramix.enoughResources(gc);
        Assertions.assertTrue(hasEnough);

        //5) now press the potion key - attack potion, check players attackpower
        KeyCode keyPressed = KeyCode.I;

        int expectedAttackPower = 3;
        Assertions.assertEquals(expectedAttackPower,asterix.getAttackpower());

    }

    @Test
    public void testInteractionWithCenturions() throws InterruptedException {
        //1) centurion is moving
        try{
            centurion.move();
        }catch(Exception e){
            //something
        }
        //test if centurion moved based on his speed on y axis
        Assertions.assertTrue(centurion.getY() > 100);

        //2)centurion comes close to asterix and attacks
        asterix.setX(105);
        asterix.setY(105);
        GameState result = asterix.checkForAttacks(asterix, romanSoldiers, centurions, 64, null, null);

        //asterix should lose health - 2
        Assertions.assertEquals(8, asterix.getHealth());
        Assertions.assertTrue(result == GameState.RUNNING);

        //3) the game should not be over
        Assertions.assertEquals(result, GameState.RUNNING);

    }
    @Test
    public void testInteractionWithRomans() throws InterruptedException {
        //1) roman is moving
        try{
            roman.move();
        }catch(Exception e){
            //something
        }
        //test if roman moved based on his speed on y axis
        Assertions.assertTrue(roman.getY() > 150);

        //2)centurion comes close to asterix and attacks
        asterix.setX(155);
        asterix.setY(155);
        GameState result = asterix.checkForAttacks(asterix, romanSoldiers, centurions, 64, null, null);
        Thread.sleep(1100);
        //asterix should lose health - 2
        Assertions.assertEquals(9, asterix.getHealth());
        Assertions.assertTrue(result == GameState.RUNNING);

        //3) the game should not be over
        Assertions.assertEquals(result, GameState.RUNNING);

    }

    @Test
    public void testAttackingCenturionsAndDroppingPotion() throws InterruptedException {
        centurion.setHealth(1);
        //1) asterix will approach centurion, he will be death, asterix will have 9 health, potion will be dropped
        asterix.setX(100);
        asterix.setY(100);
        GameState result = asterix.checkForAttacks(asterix, romanSoldiers, centurions, 64, null, null);

        Assertions.assertEquals(8, asterix.getHealth());

        //2) asterix will attack, mana should be 1, centurion should be death
        items = asterix.attack(asterix,romanSoldiers,centurions,64,items);

        int expectedMana = 4;
        Assertions.assertEquals(expectedMana, asterix.getMana());

        int expectedCenturionsListSize = 0;
        Assertions.assertEquals(expectedCenturionsListSize, centurions.size());

        //3)potion should be dropped
        Assertions.assertTrue(items.stream().anyMatch(item -> item instanceof Potion));

        //4) game should be on
        Assertions.assertEquals(result, GameState.RUNNING);

    }



    @Test
    public void endgameTestWon() throws InterruptedException {
        centurion.setHealth(1);
        roman.setHealth(1);
        //1) asterix kills centurion
        asterix.setX(100);
        asterix.setY(100);

        items = asterix.attack(asterix,romanSoldiers,centurions,64,items);
        Assertions.assertEquals(0, centurions.size());

        //2) asterix kills roman
        asterix.setX(150);
        asterix.setY(150);

        items = asterix.attack(asterix,romanSoldiers,centurions,64,items);
        Assertions.assertEquals(0, romanSoldiers.size());

        //3) should be endgame
       GameState result = asterix.checkForAttacks(asterix,romanSoldiers,centurions,64,null,null);
       GameState expectedResult = GameState.WON;
       Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void playerDeadTest() throws InterruptedException {
        asterix.setHealth(2);

        //1) asterix approaches centurion
        asterix.setX(100);
        asterix.setY(100);
        GameState result = asterix.checkForAttacks(asterix, romanSoldiers, centurions, 64, null, null);

        //2) centurion kills him -> game over
        Assertions.assertEquals(asterix.getHealth(), 0);
        Assertions.assertTrue(result == GameState.GAME_OVER);

    }
}
