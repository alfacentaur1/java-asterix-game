package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Filip Kopecký
 */

public class Menu extends Application {

    private ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * sets the main menu for level and inventory choice
     * @param primaryStage primary stage
     */
    @Override
    public void start(Stage primaryStage) {

        // vbox as container for level buttons
        primaryStage.setResizable(false);
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        //buttons
        Button level1Button = new Button("Level 1");
        level1Button.setOnAction(e -> startGame("map1.txt", "entitiesmap1.txt"));

        Button level2Button = new Button("Level 2");
        level2Button.setOnAction(e -> startGame("map2.txt", "entitiesmap2.txt"));

        RadioButton yesRadioButton = new RadioButton("Yes");
        yesRadioButton.setToggleGroup(toggleGroup);
        // Default yes choice

        RadioButton noRadioButton = new RadioButton("No");
        noRadioButton.setToggleGroup(toggleGroup);
        noRadioButton.setSelected(true);

        Text inventoryText = new Text("Load saved inventory?");

        // vbox for invenotry buttons,10px between children
        VBox inventoryChoiceBox = new VBox(15);

        inventoryChoiceBox.getChildren().addAll(inventoryText, yesRadioButton, noRadioButton);
        inventoryChoiceBox.setStyle("-fx-alignment: center;");
        inventoryText.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill:#FFD700 ; -fx-padding: 10px;");
        inventoryText.setFill(javafx.scene.paint.Color.WHITE);
        // Add buttons and inventoryChoiceBox to the main VBox
        vbox.getChildren().addAll(level1Button, level2Button, inventoryChoiceBox);

        // scene setup
        Scene scene = new Scene(vbox, 400, 640);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Level");
        primaryStage.show();
    }

    // Return "Yes" if the "Yes" radio button is selected, otherwise "No"
    private String getInventoryChoice() {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null && selectedRadioButton.isSelected()) {
            return selectedRadioButton.getText(); // Return "Yes" or "No"
        }
        return "No"; // Default if no radio button is selected
    }

    //create new instance of viewcontroller
    private void startGame(String map, String instances) {
        String inventoryChoice = getInventoryChoice();
        ViewController gameApp = new ViewController(map, instances, inventoryChoice);
        gameApp.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
