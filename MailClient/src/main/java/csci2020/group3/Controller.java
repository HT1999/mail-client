package csci2020.group3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;

import javax.mail.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    ObservableList<String> list = FXCollections.observableArrayList();

    //ObservableList<EmailListView.EmailList> data = FXCollections.observableArrayList();
    //ListView<EmailListView.EmailList> listView = new ListView<EmailListView.EmailList>(data);


    // Create 3 more ObservableList's for subject, date, from, and 1 for id to
    // facilitate interaction when list item is selected.

    @FXML
    private ListView<EmailListView.EmailList> emailList;

    @FXML
    private WebView wb = new WebView();


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

        // Send Button
        Button send_btn = new Button("Send");
        send_btn.setDefaultButton(true);
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

    // Reads emails
    public void loadButtonClicked() {
        System.out.println("User pressed load button");
        StoreEmails.ReadSentMail(preferences.getEmail(), preferences.getPassword());

        // After reading data, load to list view
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates the signIn window on button click and handles sign in (** should clean up **)
    public void signIn() throws Exception{

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

        // Collecting user email address
        Label email_lbl = new Label("Email");
        final TextField email = new TextField();
        email.setPromptText("Email");
        email.getText();
        email.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(email_lbl, 0, 0);
        GridPane.setConstraints(email, 1, 0);
        pane.getChildren().addAll(email_lbl, email);

        // Collecting User email password
        Label pwd_lbl = new Label("Password");
        final PasswordField pwd = new PasswordField();
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
                    Preferences preferences = Preferences.getPreferences();
                    preferences.setEmail(email.getText());
                    preferences.setPassword(pwd.getText());
                    preferences.initConfig();

                    System.out.println("Successful user sign in!");

                    // Successful login popup
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful Sign-In", ButtonType.OK);
                    alert.showAndWait();

                    // Invalid login attempt
                } catch (MessagingException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid email/password entered.");
                    System.out.println("Invalid user login attempt!");
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }

                // Close login window
                login_menu.close();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws Exception{

        // Opens JSON file and displays key Email information inside a ListView
        try {
            File file = new File("src/data/emails.json");
            JsonStreamParser parser = new JsonStreamParser(new FileReader(file));

            Gson gson = new GsonBuilder().create();

            // Extracting classes from json file in a list.
            Email[] email_list = gson.fromJson(parser.next(), Email[].class);

            // Creating data variable to store custom email list class elements
            ObservableList<EmailListView.EmailList> data = FXCollections.observableArrayList();

            // Listing all emails headlines in a list view (sender, subject, date)
            for (int i = email_list.length - 1; i >= 0; i--) {
                data.add(new EmailListView.EmailList(email_list[i].getFrom(), email_list[i].getSubject(), email_list[i].getDate(),
                        email_list[i].getId(), email_list[i].getContentPath()));
            }

            emailList.getItems().addAll(data);

            emailList.setCellFactory(new Callback<ListView<EmailListView.EmailList>, ListCell<EmailListView.EmailList>>() {
                @Override
                public ListCell<EmailListView.EmailList> call(ListView<EmailListView.EmailList> listView) {
                    return new EmailListView.EmailListCell();
                }
            });

            emailList.setOnMouseClicked(e -> {
                System.out.println("clicked on: " + emailList.getSelectionModel().getSelectedItems());

                // Clean WebView contents
                wb.getEngine().loadContent("");

                File testFile = new File(emailList.getSelectionModel().getSelectedItem().getPath());
                try {
                    // set WebView to emails content path
                    String str = FileUtils.readFileToString(testFile);
                    wb.getEngine().loadContent(str);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            });
        }
        catch(Exception e) {
            // emails.json file not created...
            // data needs to be loaded in
            System.out.println("Initial JSON file not created.\nData needs to be loaded in!");

        }

    }

}
