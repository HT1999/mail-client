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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller {

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
        final TextField from = new TextField();
        from.setPromptText("From");
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

        // Send Button
        Button send_btn = new Button("Send");
        send_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Temporarily using an envrionment to test email functionality
                SendEmail.sendMail(from.getText(), to.getText(), System.getenv("EMAIL2_PASS"), subject.getText(), msg.getText());
                new_email.close();
            }
        });

        GridPane.setConstraints(send_btn, 1, 4);
        GridPane.setHalignment(send_btn, HPos.RIGHT);
        pane.getChildren().add(send_btn);

        // Creating Scene and showing stage
        new_email.setTitle("New Message");
        Scene sendEmailScene = new Scene(pane,600 , 400);
        new_email.setScene(sendEmailScene);
        new_email.show();

    }
}
