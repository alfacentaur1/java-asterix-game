package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private int[][] tileMap;

    public int[][] loadMap(String filename) {
        List<int[]> tempMap = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                tempMap.add(row);
            }
        } catch (IOException e) {
            System.err.println("Exception in loading: " + e.getMessage());
            return null;
        }

        // convert it into 2d array
        tileMap = tempMap.toArray(new int[0][]);
        return tileMap;
    }


}



