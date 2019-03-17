package csci2020.group3;

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

        // Grabbing users settings information
        Preferences preferences = Preferences.getPreferences();

        // Creates a new thread to handle reading/storing of emails.
        Thread thread = new Thread(new loadThread(preferences.getEmail(), preferences.getPassword(), emailList, wb));
        thread.start();

    }

    // Creates the signInButtonClicked window on button click and handles sign in (** should clean up **)
    public void signInButtonClicked() throws Exception {
        SignIn newSignIn = new SignIn();
        newSignIn.signInButtonClicked();
    }

    // Creates about windows under help menu
    public void aboutButtonClicked() throws Exception {
        AboutHelp help = new AboutHelp();
        help.aboutButtonClicked();
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
