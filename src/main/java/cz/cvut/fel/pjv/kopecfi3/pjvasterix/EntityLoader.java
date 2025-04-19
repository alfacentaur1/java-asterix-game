package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EntityLoader {
    private static final Logger logger = Logger.getLogger(EntityLoader.class.getName());
    /**
     *this method reads a file containing entity definitions and initializes objects based on the data
     * entities are added to a list and returned
     * @param filename name of filename as String, that we are loading entities from
     * @return list of entities - characters
     */
    public ArrayList<Object> loadAllMapEntities(String filename) {
        ArrayList<Object> entities = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String type = parts[0];

                switch (type) {
                    case "Asterix" ->
                            entities.add(new Asterix(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3])
                            ));

                    case "Villager" ->
                            entities.add(new Villager(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]),
                                    Integer.parseInt(parts[7]),
                                    parts[8]
                            ));

                    case "RomanSoldier" ->
                            entities.add(new RomanSoldier(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]),
                                    parts[7]
                            ));

                    case "Obelix" ->
                            entities.add(new Obelix(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3])
                            ));

                    case "Panoramix" ->
                            entities.add(new Panoramix(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3])
                            ));

                    case "Carrot" ->
                            entities.add(new Carrot(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2])
                            ));

                    case "Shroom" ->
                            entities.add(new Shroom(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2])
                            ));

                    case "WaterBucket" ->
                            entities.add(new WaterBucket(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2])
                            ));

                    case "Centurion" ->
                            entities.add(new Centurion(
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]),
                                    Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]),
                                    parts[7]
                            ));

                    default -> logger.warning("Unknown entity: " + type);
                }
            }
        } catch (IOException e) {
        }
        return entities;
    }
}
