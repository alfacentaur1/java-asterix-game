package UnitTests;

import cz.cvut.fel.pjv.kopecfi3.pjvasterix.FileLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoaderTest {

    @Test
    public void testLoadMap_successfulLoad() throws IOException {
        // Create a temporary file with a simple map
        String mapContent = """
                1 2 3
                4 5 6
                7 8 9
                """;
        Path tempFile = Files.createTempFile("map", ".txt");
        Files.writeString(tempFile, mapContent);

        FileLoader loader = new FileLoader();
        int[][] result = loader.loadMap(tempFile.toString());

        // Verify the structure and content of the loaded map
        assertNotNull(result);
        assertEquals(3, result.length); // 3 rows
        assertArrayEquals(new int[]{1, 2, 3}, result[0]);
        assertArrayEquals(new int[]{4, 5, 6}, result[1]);
        assertArrayEquals(new int[]{7, 8, 9}, result[2]);

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testLoadMapFileNotFound() {
        FileLoader loader = new FileLoader();
        int[][] result = loader.loadMap("non_existing_file.txt");

        // Expecting null when file doesn't exist
        assertNull(result);
    }

    @Test
    public void testLoadMapWithEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("empty_map", ".txt");
        Files.writeString(tempFile, ""); // write nothing

        FileLoader loader = new FileLoader();
        int[][] result = loader.loadMap(tempFile.toString());

        assertNotNull(result);
        assertEquals(0, result.length);

        Files.deleteIfExists(tempFile);
    }
}

