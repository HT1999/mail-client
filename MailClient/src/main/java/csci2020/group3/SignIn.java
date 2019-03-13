package csci2020.group3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SignIn {

    // Creates the signInButtonClicked window on button click and handles sign in (** should clean up **)
    public void signInButtonClicked() throws Exception{

        // Might use ******
        // FXML Setup
//        final Stage login_menu = new Stage();
//        login_menu.initModality(Modality.APPLICATION_MODAL);
//
//        Parent login_disp = FXMLLoader.load(getClass().getResource("signin.fxml"));
//
//        Scene sendEmailScene = new Scene(login_disp,600 , 400);
//        login_menu.setScene(sendEmailScene);
//        login_menu.show();

        System.out.println("User attempting to login");

        // Create new send email window
        final Stage login_menu = new Stage();
        login_menu.initModality(Modality.APPLICATION_MODAL);
        //dialog.initOwner(primaryStage);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(30, 10, 10, 25));
        pane.setHgap(10);
        pane.setVgap(10);

        pane.setStyle("-fx-background-color: #424242");

        // Collecting user email address
        Label email_lbl = new Label("Email");
        email_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final TextField email = new TextField();
        email.setStyle("-fx-control-inner-background:#757575;" + "-fx-text-fill: #FFFFFF");
        email.setPromptText("Email");
        email.getText();
        email.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(email_lbl, 0, 0);
        GridPane.setConstraints(email, 1, 0);
        pane.getChildren().addAll(email_lbl, email);

        // Collecting User email password
        Label pwd_lbl = new Label("Password");
        pwd_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final PasswordField pwd = new PasswordField();
        pwd.setStyle("-fx-control-inner-background:#757575;" + "-fx-text-fill: #FFFFFF");
        pwd.setPromptText("Password");
        pwd.getText();
        pwd.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(pwd_lbl, 0, 1);
        GridPane.setConstraints(pwd, 1, 1);
        pane.getChildren().addAll(pwd_lbl, pwd);

        // Login Button
        Button login_btn = new Button("Login");
        login_btn.setPrefHeight(35);
        login_btn.setPrefWidth(100);
        login_btn.setDefaultButton(true);
        login_btn.setTextFill(Paint.valueOf("#FFFFFF"));

        // Styling the default send button
        login_btn.setStyle("-fx-background-color: #757575;" +
                "-fx-border-color: #9E9E9E;\n" + "-fx-border-radius: 1;");

        // Styling on different mouse events
        login_btn.setOnMouseEntered(e -> login_btn.setStyle("-fx-background-color: #9E9E9E;" +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));
        login_btn.setOnMouseExited(e -> login_btn.setStyle("-fx-background-color: #757575;"  +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Authenticating..
                Properties props = new Properties();
                props.put("mail.smtp.host", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.transport.protocol", "smtp");


                Session emailSession = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        email.getText(), pwd.getText());
                            }
                        });
                // emailSession.setDebug(true);

                try {
                    emailSession.getTransport().connect();

                    // Updates config file
                    Preferences preferences = new Preferences(email.getText(), pwd.getText());
                    preferences.initConfig();

                    System.out.println("Successful user sign in!");

                    // Successful login popup
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful Sign-In", ButtonType.OK);
                    alert.showAndWait();

                    // Close login window
                    login_menu.close();

                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // *******
                    // On first sign-in, load users email data (temp fix, config.txt isn't being read
                    // until the app is closed. reading from the data fields fixes that)

                    StoreEmails.storeEmails(preferences.getEmail(), preferences.getPassword());

                    try {
                        Controller con = new Controller();
                        con.loadData();
                        //Controller.loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //System.out.println(preferences.getEmail() + "\n" + preferences.getPassword());
//                    if (preferences.getEmail() == null || preferences.getPassword() == null) {
//                        StoreEmails.storeEmails(email.getText(), pwd.getText());
//                    }

                    // Invalid login attempt
                } catch (MessagingException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid email/password entered.");
                    System.out.println("Invalid user login attempt!");
                    errorAlert.showAndWait();
                    //e.printStackTrace();
                }

                // Close login window
                //login_menu.close();
            }
        });

        GridPane.setConstraints(login_btn, 1, 4);
        GridPane.setHalignment(login_btn, HPos.CENTER);
        pane.getChildren().add(login_btn);

        // Creating Scene and showing stage
        login_menu.setTitle("Sign In");
        Scene sendEmailScene = new Scene(pane,270 , 180);
        login_menu.setScene(sendEmailScene);
        login_menu.show();

    }
}
