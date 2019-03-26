package csci2020.group3;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

// Class that handles composing a new email
public class CreateNewEmail {

    Preferences preferences = Preferences.getPreferences();
    public static String attach_path;
    public static TextField attach_field = new TextField();

    // onclick method to generate new email window
    public void newButtonClicked(String receiver, String subj, String content) {

        // Create new send email window
        final Stage new_email = new Stage();
        new_email.initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(30, 10, 10, 20));
        pane.setHgap(10);
        pane.setVgap(10);

        pane.setStyle("-fx-background-color: #424242");

        // From email
        Label from_lbl = new Label("From");
        from_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final TextField from = new TextField(preferences.getEmail());
        from.setStyle("-fx-background-color: #757575;" + "-fx-text-fill: #FFFFFF");
        from.getText();
        from.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(from_lbl, 0, 0);
        GridPane.setConstraints(from, 1, 0);
        pane.getChildren().addAll(from_lbl, from);

        // To email
        Label to_lbl = new Label("To");
        to_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final TextField to = new TextField();
        to.setStyle("-fx-background-color: #757575;" + "-fx-text-fill: #FFFFFF");
        to.setPromptText("Recipients");
        to.getText();

        if (receiver != null) {
            to.setText(receiver);
        }
        to.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(to_lbl, 0, 1);
        GridPane.setConstraints(to, 1, 1);
        pane.getChildren().addAll(to_lbl, to);

        // Subject
        Label subject_lbl = new Label("Subject");
        subject_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final TextField subject = new TextField();
        subject.setStyle("-fx-background-color: #757575;" + "-fx-text-fill: #FFFFFF");
        subject.setPromptText("Subject");
        subject.getText();

        if (subject != null) {
            subject.setText(subj);
        }
        to.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(subject_lbl, 0, 2);
        GridPane.setConstraints(subject, 1, 2);
        pane.getChildren().addAll(subject_lbl, subject);

        // Message
        Label msg_lbl = new Label("Message");
        msg_lbl.setStyle("-fx-text-fill: #FFFFFF");
        final TextArea msg = new TextArea();
        msg.setStyle("-fx-control-inner-background:#757575;" + "-fx-text-fill: #FFFFFF");
        msg.setPromptText("Message");
        msg.getText();

        if (msg != null) {
            msg.setText(content);
        }
        GridPane.setConstraints(msg_lbl, 0, 3);
        GridPane.setConstraints(msg, 1, 3);
        pane.getChildren().addAll(msg_lbl, msg);

        // Attach File
        HBox attach_hbox = new HBox();

        attach_field.setBackground(Background.EMPTY);
        attach_field.setText("");
        attach_field.setStyle("-fx-text-fill: #FFFFFF");

        Button attach_btn = new Button();
        // Styling the default attach button
        attach_btn.setStyle("-fx-background-color: #757575;" +
                            "-fx-border-color: #9E9E9E;\n" + "-fx-border-radius: 1;");
        attach_btn.setTextFill(Paint.valueOf("#FFFFFF"));

        // Styling on different mouse events
        attach_btn.setOnMouseEntered(e -> attach_btn.setStyle("-fx-background-color: #9E9E9E;" +
                                                                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));
        attach_btn.setOnMouseExited(e -> attach_btn.setStyle("-fx-background-color: #757575;"  +
                                                                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));

        attach_btn.setGraphic(new ImageView("csci2020/group3/link.png"));

        attach_btn.setOnAction(e -> {

            // MacOS is picky with Swing objects. Had to disable MacOS from
            // attaching files to emails otherwise the application hangs.
            String osName = System.getProperty("os.name");
            if (osName.equals("Mac OS X")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Unfortunately attachments cannot be added " +
                                                                                "on your operating system. (MacOS)");
                System.out.println("JFileChooser causes macOS to crash...\n" +
                                    "Unfortunately emails cannot be attached on your operating system.");
                errorAlert.showAndWait();
            }
            else {
                try {
                    EventQueue.invokeAndWait(() -> {
                        // Uses JFileChooser interface for user to select the attachment file.
                        JFileChooser finder = new JFileChooser();
                        finder.showOpenDialog(null);
                        File f = finder.getSelectedFile();

                        // Outputs file names to textfield
                        attach_path = f.getAbsolutePath();
                        attach_field.setText(f.getName());
                    });
                } catch (InterruptedException e1) {
                    // User closes email attachment window - no need for a stackTrace
                    //e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    // Redundant exception, needed for invokeAndWait.
                    //e1.printStackTrace();
                }
            }
        });

        attach_hbox.getChildren().addAll(attach_btn, attach_field);

        GridPane.setHalignment(attach_hbox, HPos.LEFT);
        GridPane.setConstraints(attach_hbox, 1, 4);
        pane.getChildren().addAll(attach_hbox);

        // Cancel Button (closes new email window)
        Button cancel_btn = new Button("Cancel");
        cancel_btn.setTextFill(Paint.valueOf("#FFFFFF"));

        // Styling the default cancel button
        cancel_btn.setStyle("-fx-background-color: #757575;" +
                "-fx-border-color: #9E9E9E;\n" + "-fx-border-radius: 1;");

        // Styling on different mouse events
        cancel_btn.setOnMouseEntered(e -> cancel_btn.setStyle("-fx-background-color: #9E9E9E;" +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));
        cancel_btn.setOnMouseExited(e -> cancel_btn.setStyle("-fx-background-color: #757575;"  +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));

        cancel_btn.setOnAction(e -> {
            Stage stage = (Stage) cancel_btn.getScene().getWindow();
            stage.close();
        });

        GridPane.setConstraints(cancel_btn, 0, 5);
        GridPane.setHalignment(cancel_btn, HPos.LEFT);
        pane.getChildren().add(cancel_btn);

        // Send Button
        Button send_btn = new Button("Send");
        send_btn.setTextFill(Paint.valueOf("#FFFFFF"));

        // Styling the default send button
        send_btn.setStyle("-fx-background-color: #757575;" +
                "-fx-border-color: #9E9E9E;\n" + "-fx-border-radius: 1;");

        // Styling on different mouse events
        send_btn.setOnMouseEntered(e -> send_btn.setStyle("-fx-background-color: #9E9E9E;" +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));
        send_btn.setOnMouseExited(e -> send_btn.setStyle("-fx-background-color: #757575;"  +
                "-fx-border-color: #9E9E9E;" + "-fx-border-radius: 1;"));

        send_btn.setOnAction(event -> {
            SendEmail.sendMail(from.getText(), to.getText(), preferences.getPassword(), subject.getText(), msg.getText());
            new_email.close();
        });

        // Set final features to pane
        GridPane.setConstraints(send_btn, 1, 5);
        GridPane.setHalignment(send_btn, HPos.RIGHT);
        pane.getChildren().add(send_btn);

        // Creating Scene and showing stage
        new_email.setTitle("New Message");
        Scene sendEmailScene = new Scene(pane,650 , 400);
        new_email.setScene(sendEmailScene);
        new_email.show();

    }
}
