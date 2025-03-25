package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

public class Menu extends Application {

    private ToggleGroup toggleGroup = new ToggleGroup();

    @Override
    public void start(Stage primaryStage) {

        //vbox - all elements under, 10px away
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Buttons for levels
        Button level1Button = new Button("Level 1");
        level1Button.setOnAction(e -> startGame("map1.txt", "entitiesmap1.txt"));

        Button level2Button = new Button("Level 2");
        level2Button.setOnAction(e -> startGame("map2.txt", "entitiesmap2.txt"));

        // RadioButton for "Yes"
        RadioButton yesRadioButton = new RadioButton("Yes");
        yesRadioButton.setToggleGroup(toggleGroup);
        //default yes choice
        yesRadioButton.setSelected(true);

        // RadioButton for "No"
        RadioButton noRadioButton = new RadioButton("No");
        noRadioButton.setToggleGroup(toggleGroup);

        // Add radio buttons to layout
        HBox inventoryChoiceBox = new HBox(10);
        Text inventoryText = new Text("Load saved inventory?");
        inventoryChoiceBox.getChildren().addAll(inventoryText,yesRadioButton, noRadioButton);


        // Add buttons and radio buttons to VBox
        vbox.getChildren().addAll(level1Button, level2Button, inventoryChoiceBox);


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

    private void startGame(String map, String instances) {
        String inventoryChoice = getInventoryChoice();
        ViewController gameApp = new ViewController(map, instances, inventoryChoice);
        gameApp.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
