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

    ObservableList<String> list = FXCollections.observableArrayList();

    //ObservableList<EmailListView.EmailList> data = FXCollections.observableArrayList();
    //ListView<EmailListView.EmailList> listView = new ListView<EmailListView.EmailList>(data);

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

        StoreEmails.storeEmails(preferences.getEmail(), preferences.getPassword());

        // After reading data, load to list view
        try {
            loadListViewData();
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
            loadListViewData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fills Emails ListView with data
    public void loadListViewData() throws Exception{
        LoadEmailListView.loadData(emailList, wb);
    }

}
