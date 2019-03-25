package csci2020.group3;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

// Class Displays an about page
public class AboutHelp {
    public void aboutButtonClicked() throws Exception{
        System.out.println("User clicked Help>About button");

        // Create new popup window
        final Stage about_window = new Stage();
        about_window.initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        // Apply dark scheme to about window
        pane.setStyle("-fx-background-color: #424242");

        // Add text to be shown in window
        final Label aboutInfo = new Label("Mail Client");
        final Label created = new Label("Created by: ");
        final Label spencer = new Label("\tSpencer Gray");
        final Label hassan = new Label("\tHassan Tariq");
        final Label sailesh = new Label("\tSailesh Sharma");

        // Apply dark scheme to text
        aboutInfo.setStyle("-fx-text-fill: #FFFFFF");
        created.setStyle("-fx-text-fill: #FFFFFF");
        spencer.setStyle("-fx-text-fill: #FFFFFF");
        hassan.setStyle("-fx-text-fill: #FFFFFF");
        sailesh.setStyle("-fx-text-fill: #FFFFFF");

        // Change title font
        aboutInfo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        // Place text in correct positions
        GridPane.setConstraints(aboutInfo, 0, 0);
        GridPane.setConstraints(created, 0, 2);
        GridPane.setConstraints(spencer, 0, 3);
        GridPane.setConstraints(hassan, 0, 4);
        GridPane.setConstraints(sailesh, 0, 5);

        pane.getChildren().addAll(aboutInfo, created, spencer, hassan, sailesh);

        // Creating Scene and showing stage
        about_window.setTitle("About");
        Scene aboutScene = new Scene(pane,270 , 180);
        about_window.setScene(aboutScene);
        about_window.show();
    }
}
