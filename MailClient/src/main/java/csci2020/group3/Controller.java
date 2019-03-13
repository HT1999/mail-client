package csci2020.group3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    ObservableList<String> list = FXCollections.observableArrayList();

    //ObservableList<EmailListView.EmailList> data = FXCollections.observableArrayList();
    //ListView<EmailListView.EmailList> listView = new ListView<EmailListView.EmailList>(data);

    @FXML
    public ListView<EmailListView.EmailList> emailList;

    @FXML
    private WebView wb = new WebView();

    // onclick method to generate new email window
    public void newButtonClicked() {
        CreateNewEmail newEmail = new CreateNewEmail();
        newEmail.newButtonClicked();
    }

    // Reads emails
    public void loadButtonClicked() {
        System.out.println("User pressed load button");

        Preferences preferences = Preferences.getPreferences();

        StoreEmails.storeEmails(preferences.getEmail(), preferences.getPassword());

        // After reading data, load to list view
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates the signInButtonClicked window on button click and handles sign in (** should clean up **)
    public void signInButtonClicked() throws Exception {
        SignIn newSignIn = new SignIn();
        newSignIn.signInButtonClicked();
    }

    //
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() throws Exception{


        // Opens JSON file and displays key Email information inside a ListView
        try {
            File file = new File("src/data/emails.json");
            JsonStreamParser parser = new JsonStreamParser(new FileReader(file));

            Gson gson = new GsonBuilder().create();

            // Extracting classes from json file in a list.
            Email[] email_list = gson.fromJson(parser.next(), Email[].class);

            // Creating data variable to store custom email list class elements
            ObservableList<EmailListView.EmailList> data = FXCollections.observableArrayList();

            // Cleans email ListView, otherwise data overwrites and gets duplicated
            // *** needs work, should'nt have to parse all emails every time you load ***
            emailList.getItems().clear();


            // Listing all emails headlines in a list view (sender, subject, date)
            for (int i = email_list.length - 1; i >= 0; i--) {
                data.add(new EmailListView.EmailList(email_list[i].getFrom(), email_list[i].getSubject(), email_list[i].getDate(),
                        email_list[i].getId(), email_list[i].getContentPath()));
            }

            emailList.getItems().addAll(data);

            // Needed to implement custom email cell design
            emailList.setCellFactory(new Callback<ListView<EmailListView.EmailList>, ListCell<EmailListView.EmailList>>() {
                @Override
                public ListCell<EmailListView.EmailList> call(ListView<EmailListView.EmailList> listView) {
                    return new EmailListView.EmailListCell();
                }
            });

            //emailList.scroll

            // Opening ListView items HTML contents in the WebView
            emailList.setOnMouseClicked(e -> {
                System.out.println("clicked on: " + emailList.getSelectionModel().getSelectedItems());

                // Clean WebView contents
                wb.getEngine().loadContent("");


                File testFile = new File(emailList.getSelectionModel().getSelectedItem().getPath());
                try {
                    // set WebView to emails content path
                    String str =  FileUtils.readFileToString(testFile, "UTF-8");
                    wb.getEngine().loadContent(str);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            });
        }
        catch(Exception e) {
            // emails.json file not created...
            System.out.println("Initial JSON file not created.\nData needs to be loaded in!");
            e.printStackTrace();
        }

    }

}
