package UnitTests;
import cz.cvut.fel.pjv.kopecfi3.pjvasterix.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class EntityLoaderTest {

    @Test
    public void testLoadAllMapEntities() throws IOException {
        // 1. Create temp files simulating txt file
        String content = """
                Asterix 1 2 3
                Obelix 4 5
                Villager 6 7 8 9 10 11 12 Jean
                RomanSoldier 13 14 15 16 17 18 Marcus
                Panoramix 19 20
                Carrot 21 22
                Shroom 23 24
                WaterBucket 25 26
                Centurion 27 28 29 30 31 32 Maximus
                """;

        Path tempFile = Files.createTempFile("entities", ".txt");
        Files.writeString(tempFile, content);

        // 2. Load entities
        EntityLoader loader = new EntityLoader();
        ArrayList<Object> entities = loader.loadAllMapEntities(tempFile.toString());

        // 3. Assert
        assertEquals(9, entities.size());
        assertTrue(entities.get(0) instanceof Asterix);
        assertTrue(entities.get(1) instanceof Obelix);
        assertTrue(entities.get(2) instanceof Villager);
        assertTrue(entities.get(3) instanceof RomanSoldier);
        assertTrue(entities.get(4) instanceof Panoramix);
        assertTrue(entities.get(5) instanceof Carrot);
        assertTrue(entities.get(6) instanceof Shroom);
        assertTrue(entities.get(7) instanceof WaterBucket);
        assertTrue(entities.get(8) instanceof Centurion);


        // 4. Delete temp file
        Files.deleteIfExists(tempFile);
    }
}