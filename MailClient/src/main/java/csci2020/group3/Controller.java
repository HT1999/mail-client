package csci2020.group3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static csci2020.group3.Preferences.CONFIG_FILE;


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
    @FXML
    ProgressBar loading;
    @FXML
    MenuItem signinMenu;
    @FXML
    MenuItem signoutMenu;

    // Keeps track of current mailbox being viewed in the application (helper for email deletion)
    private String mailboxCurrent = "INBOX";


    // onclick method to generate new email window
    public void newButtonClicked() {
        CreateNewEmail newEmail = new CreateNewEmail();
        newEmail.newButtonClicked(null, null, null);
    }

    // Reads emails
    public void loadButtonClicked() {

        // Grabbing users settings information
        Preferences preferences = Preferences.getPreferences();

        // Creates a new thread to handle reading/storing of emails.
        Thread thread = new Thread(new loadThread(preferences.getEmail(), preferences.getPassword(), emailList, wb, searchField, loading));
        thread.start();

    }

    // Creates the signInButtonClicked window on button click and handles sign in.
    public void signInButtonClicked() throws Exception {
        // Initializing array of the main window buttons to disable for un-authorized users
        Button[] buttons = {newEmailBtn, loadBtn, replyBtn, fwdBtn, deleteBtn, inboxBtn,
                sentBtn, starredBtn, spamBtn, trashBtn};

        // Running signing method on a separate thread
        Thread signinThread = new Thread(new SigninThread(buttons, signinMenu, signoutMenu));
        signinThread.start();
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

    // Opens new email window with to field auto filled with selected email
    public void replyButtonClicked() {
        CreateNewEmail newEmail = new CreateNewEmail();

        if (emailList.getSelectionModel().getSelectedItem() != null) {
            newEmail.newButtonClicked(emailList.getSelectionModel().getSelectedItem().getName(), null, null);
        }
    }

    // Creates about windows under help menu
    public void deleteButtonClicked() throws Exception {
        int emailIndex = emailList.getSelectionModel().getSelectedItem().getId();
        DeleteEmail deleteBtn = new DeleteEmail(emailList, emailIndex, mailboxCurrent, wb, searchField);
        deleteBtn.deleteButtonClicked();
    }

    public void forwardButtonClicked() {
        CreateNewEmail newEmail = new CreateNewEmail();

        // Writing emailList path contents to a string
        String content;
        try {
            content = new Scanner(new File(emailList.getSelectionModel().getSelectedItem().getPath())).useDelimiter("\\Z").next();
            if (emailList.getSelectionModel().getSelectedItem() != null) {
                newEmail.newButtonClicked(null, emailList.getSelectionModel().getSelectedItem().getSubject(), content);
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
    }

    // Initializes Buttons, MenuItems, and the ListView
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initializing array of the main window buttons to disable for un-authorized users
        Button[] buttons = {newEmailBtn, loadBtn, replyBtn, fwdBtn, deleteBtn, inboxBtn,
                sentBtn, starredBtn, spamBtn, trashBtn};
        ButtonState bs = new ButtonState();

        File configFile = new File(CONFIG_FILE);
        File inboxFolder = new File("src/data/INBOX");
        File gmailFolder = new File("src/data/[Gmail]");

        // Initialize progress bar at 0
        loading.setProgress(0f);

        // Initialize WebView
        wb.getEngine().loadContent("<!DOCTYPE html><html><body bgcolor=\"#212121\"></body></html>");

        // If config.txt doesnt exist but mailbox folders do for whatever reason (deletion failure), empty local folders
        if ((!configFile.exists()) && (inboxFolder.exists() || gmailFolder.exists())) {
            try {
                FileUtils.deleteDirectory(inboxFolder);
                FileUtils.deleteDirectory(gmailFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bs.setMenuBarItems(signinMenu, signoutMenu);
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

    // Method for sidebar buttons, activated when clicked
    @FXML
    public void loadListViewDataInbox() throws Exception {
        mailboxCurrent = "INBOX";
        LoadEmailListView.loadData(emailList, wb, "INBOX", searchField);
    }

    // Method for Sent button
    @FXML
    public void loadListViewDataSent() throws Exception {
        mailboxCurrent = "[Gmail]/Sent Mail";
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Sent Mail", searchField);
    }

    // Method for Starred button
    @FXML
    public void loadListViewDataStarred() throws Exception {
        mailboxCurrent = "[Gmail]/Starred";
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Starred", searchField);
    }

    // Method for Spam button
    @FXML
    public void loadListViewDataSpam() throws Exception {
        mailboxCurrent = "[Gmail]/Spam";
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Spam", searchField);
    }

    // Method for Trash button
    @FXML
    public void loadListViewDataTrash() throws Exception {
        mailboxCurrent = "[Gmail]/Trash";
        LoadEmailListView.loadData(emailList, wb, "[Gmail]/Trash", searchField);
    }

}
