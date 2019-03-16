package csci2020.group3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    @FXML
    public ListView<EmailListView.EmailList> emailList = new ListView<>();

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

        // Creating an array of the different mailboxes to stage for loading
        String[] mailboxList = new String[5];
        mailboxList[0] = "INBOX";
        mailboxList[1] = "[Gmail]/Sent Mail";
        mailboxList[2] = "[Gmail]/Trash";
        mailboxList[3] = "[Gmail]/Spam";
        mailboxList[4] = "[Gmail]/Starred";

        // Iterating through the mailboxes and storing them
        for (int i = 0; i < 5; i++) {
            StoreEmails.storeEmails(preferences.getEmail(), preferences.getPassword(), mailboxList[i]);
        }

        // After reading data, load to list view
        try {
            // First loads Inbox by default
            loadListViewDataInbox();
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
            // Loads Inbox by default
            loadListViewDataInbox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Have to use separate methods for fxml buttons
    // Method for Inbox button
    @FXML
    public void loadListViewDataInbox() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "INBOX");
    }

    // Method for Sent button
    @FXML
    public void loadListViewDataSent() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Sent Mail");
    }

    // Method for Starred button
    @FXML
    public void loadListViewDataStarred() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Starred");
    }

    // Method for Spam button
    @FXML
    public void loadListViewDataSpam() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Spam");
    }

    // Method for Trash button
    @FXML
    public void loadListViewDataTrash() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Trash");
    }

}
