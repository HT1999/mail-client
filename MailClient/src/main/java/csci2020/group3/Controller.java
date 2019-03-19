package csci2020.group3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    @FXML
    public ListView<EmailListView.EmailList> emailList = new ListView<>();

    @FXML
    private WebView wb = new WebView();

    @FXML
    private TextField searchField = new TextField();

    // Main window buttons
    @FXML
    Button newEmailBtn;
    @FXML
    Button loadBtn;
    @FXML
    Button replyBtn;
    @FXML
    Button fwdBtn;
    @FXML
    Button deleteBtn;
    @FXML
    Button inboxBtn;
    @FXML
    Button sentBtn;
    @FXML
    Button starredBtn;
    @FXML
    Button spamBtn;
    @FXML
    Button trashBtn;

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
        Thread thread = new Thread(new loadThread(preferences.getEmail(), preferences.getPassword(), emailList, wb, searchField));
        thread.start();

    }

    // Creates the signInButtonClicked window on button click and handles sign in.
    public void signInButtonClicked() throws Exception {
        // Initializing array of the main window buttons to disable for un-authorized users
        Button[] buttons = {newEmailBtn, loadBtn, replyBtn, fwdBtn, deleteBtn, inboxBtn,
                sentBtn, starredBtn, spamBtn, trashBtn};
        //ButtonState bs = new ButtonState();
        SignIn newSignIn = new SignIn(buttons);
        newSignIn.signInButtonClicked();
    }

    public void signOutButtonClicked() {
        SignOut so = new SignOut();
        so.signOut();

        // Closes application
        Stage stage = (Stage) emailList.getScene().getWindow();
        stage.close();
    }

    public void closeButtonClicked() {
        Stage stage = (Stage) emailList.getScene().getWindow();
        stage.close();
    }

    // Creates about windows under help menu
    public void aboutButtonClicked() throws Exception {
        AboutHelp help = new AboutHelp();
        help.aboutButtonClicked();
    }

    //
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializing array of the main window buttons to disable for un-authorized users
        Button[] buttons = {newEmailBtn, loadBtn, replyBtn, fwdBtn, deleteBtn, inboxBtn,
                sentBtn, starredBtn, spamBtn, trashBtn};
        ButtonState bs = new ButtonState();

        try {
            bs.setButtons(buttons);
        } catch(Exception e) {
            e.printStackTrace();
        }

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
        LoadEmailListView.loadData(emailList, wb, "INBOX", searchField);
    }

    // Method for Sent button
    @FXML
    public void loadListViewDataSent() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Sent Mail", searchField);
    }

    // Method for Starred button
    @FXML
    public void loadListViewDataStarred() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Starred", searchField);
    }

    // Method for Spam button
    @FXML
    public void loadListViewDataSpam() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Spam", searchField);
    }

    // Method for Trash button
    @FXML
    public void loadListViewDataTrash() throws Exception {
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Trash", searchField);
    }


}
