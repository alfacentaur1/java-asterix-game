package UnitTests;

import cz.cvut.fel.pjv.kopecfi3.pjvasterix.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class InventoryTest {
    ArrayList<Item> items;
    Asterix asterix;
    Inventory inventory;
    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
        asterix = Mockito.mock(Asterix.class);
        inventory = new Inventory();
    }

    /**
     * Test when player is in range of item and not full inventory
     */
    @Test
    public void searchItemsTestReturnsItem(){
        Carrot carrot = Mockito.mock(Carrot.class);
        items.add(carrot);

        Mockito.when(carrot.getY()).thenReturn(2.0);
        Mockito.when(carrot.getX()).thenReturn(2.0);

        Mockito.when(asterix.getY()).thenReturn(2.0);
        Mockito.when(asterix.getX()).thenReturn(2.0);

        Item expectedItem = carrot;

        Item returnedItem = inventory.searchItems(items,1,1,64,asterix,inventory);

        Assertions.assertEquals(expectedItem, returnedItem);
    }

    /**
     * Test full inventory
     */
    @Test
    public void searchItemTestFullInventory(){
        Inventory mockedInventory = Mockito.mock(Inventory.class);
        Mockito.when(mockedInventory.getSize()).thenReturn(6);
        Item returnedItem = inventory.searchItems(items,1,1,64,asterix,mockedInventory);
        Item expectedReturnedItem = null;
        Assertions.assertEquals(expectedReturnedItem, returnedItem);
    }

    @Test
    public void searchItemTestEmptyInventory(){
        Item expectedReturnedItem = null;

        Item returnedItem = inventory.searchItems(items,1,1,64,asterix,inventory);
        Assertions.assertEquals(expectedReturnedItem, returnedItem);

    }

    /**
     * Test if item is potion, then it should be removed from arraylist of game items
     */
    @Test
    public void searchItemTestPotionItem(){
        Potion potion = Mockito.mock(Potion.class);
        items.add(potion);

        Mockito.when(potion.getY()).thenReturn(2.0);
        Mockito.when(potion.getX()).thenReturn(2.0);

        Mockito.when(asterix.getY()).thenReturn(2.0);
        Mockito.when(asterix.getX()).thenReturn(2.0);

        int expectedListSize = 0;

        inventory.searchItems(items,1,1,64,asterix,inventory);

        Assertions.assertEquals(expectedListSize, inventory.getSize());
    }
}
