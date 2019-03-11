package csci2020.group3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class CreateNewEmail {

    Preferences preferences = Preferences.getPreferences();
    public static String attach_path;
    public static TextField attach_field = new TextField();

    // onclick method to generate new email window
    public void newButtonClicked() {
        System.out.println("User pressed new button...");

        // Create new send email window
        final Stage new_email = new Stage();
        new_email.initModality(Modality.APPLICATION_MODAL);
        //dialog.initOwner(primaryStage);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(30, 10, 10, 20));
        pane.setHgap(10);
        pane.setVgap(10);

        // From email ** Temporary, should be autofilled by current users email
        Label from_lbl = new Label("From");
        final TextField from = new TextField(preferences.getEmail());
        from.getText();
        from.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(from_lbl, 0, 0);
        GridPane.setConstraints(from, 1, 0);
        pane.getChildren().addAll(from_lbl, from);

        // To email
        Label to_lbl = new Label("To");
        final TextField to = new TextField();
        to.setPromptText("Recipients");
        to.getText();
        to.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(to_lbl, 0, 1);
        GridPane.setConstraints(to, 1, 1);
        pane.getChildren().addAll(to_lbl, to);

        // Subject
        Label subject_lbl = new Label("Subject");
        final TextField subject = new TextField();
        subject.setPromptText("Subject");
        subject.getText();
        to.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(subject_lbl, 0, 2);
        GridPane.setConstraints(subject, 1, 2);
        pane.getChildren().addAll(subject_lbl, subject);

        // Message
        Label msg_lbl = new Label("Message");
        final TextArea msg = new TextArea();
        msg.setPromptText("Message");
        msg.getText();
        GridPane.setConstraints(msg_lbl, 0, 3);
        GridPane.setConstraints(msg, 1, 3);
        pane.getChildren().addAll(msg_lbl, msg);

        // Attach File
        HBox attach_hbox = new HBox();

        attach_field.setBackground(Background.EMPTY);
        attach_field.setText("");

        Button attach_btn = new Button();
        attach_btn.setGraphic(new ImageView("csci2020/group3/link.png"));
        attach_btn.setOnAction(e -> {

            // MacOS is picky with Swing, had to use
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        // Uses JFileChooser interface for user to select the attachment file.
                        JFileChooser finder = new JFileChooser();
                        finder.showOpenDialog(null);
                        File f = finder.getSelectedFile();

                        // Outputs file names to textfield
                        attach_path = f.getAbsolutePath();
                        attach_field.setText(f.getName());
                    }
                });
            } catch (InterruptedException e1) {
                // User closes email attachment window.
                //e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                // Redundant exception, needed for invokeAndWait.
                //e1.printStackTrace();
            }
        });

        attach_hbox.getChildren().addAll(attach_btn, attach_field);

        GridPane.setHalignment(attach_hbox, HPos.LEFT);
        GridPane.setConstraints(attach_hbox, 1, 4);
        pane.getChildren().addAll(attach_hbox);

        // Cancel Button (closes new email window)
        Button cancel_btn = new Button("Cancel");
        cancel_btn.setOnAction(e -> {
            Stage stage = (Stage) cancel_btn.getScene().getWindow();
            stage.close();
        });

        GridPane.setConstraints(cancel_btn, 0, 5);
        GridPane.setHalignment(cancel_btn, HPos.LEFT);
        pane.getChildren().add(cancel_btn);

        // Send Button
        Button send_btn = new Button("Send");
        //send_btn.setDefaultButton(true);
        send_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SendEmail.sendMail(from.getText(), to.getText(), preferences.getPassword(), subject.getText(), msg.getText());
                new_email.close();
            }
        });

        GridPane.setConstraints(send_btn, 1, 5);
        GridPane.setHalignment(send_btn, HPos.RIGHT);
        pane.getChildren().add(send_btn);

        // Creating Scene and showing stage
        new_email.setTitle("New Message");
        Scene sendEmailScene = new Scene(pane,600 , 400);
        new_email.setScene(sendEmailScene);
        new_email.show();

    }
}
