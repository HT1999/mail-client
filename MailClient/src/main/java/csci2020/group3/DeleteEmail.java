package csci2020.group3;


import com.google.gson.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

// Class to implement delete function in
@SuppressWarnings("unchecked")
public class DeleteEmail {

    private static Folder folder;
    private ListView<EmailListView.EmailList> emailList;
    private int index;
    private String mailbox;
    private WebView wb;
    private TextField searchField;

    DeleteEmail(ListView emailList, int index, String mailbox, WebView wb, TextField searchField) {
        this.emailList = emailList;
        this.index = index;
        this.mailbox = mailbox;
        this.wb = wb;
        this.searchField = searchField;
    }

    public void deleteButtonClicked() throws Exception {
        System.out.println("User clicked delete button");

        // Open mailserver to delete from
        Properties props = System.getProperties();
        props.put("mail.imap.fetchsize", "100000");
        props.setProperty("mail.imap.partialfetch", "false");
        props.setProperty("mail.imaps.partialfetch", "false");

        // Initializing session and store var
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");

        // Getting authentication info from "config.txt"
        Preferences preferences = Preferences.getPreferences();

        store.connect("imap.gmail.com", preferences.getEmail(), preferences.getPassword());

        // Loads specified folder and prints total number of emails inside
        folder = store.getFolder(mailbox);

        // Opening store folder
        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }

        // fetch read messages
        Message read_messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));

        // Opening trash folder
        Folder trashFolder = store.getFolder("[Gmail]/Trash");

        boolean found = false;

        for (int i = 0; i < read_messages.length; i++) {
            if (read_messages[i].getMessageNumber() == index) {

                found = true;

                // Setting flag on message for deletion
                read_messages[i].setFlag(Flags.Flag.DELETED, true);

                System.out.println("Deleting message: " + read_messages[i].getSubject());

                // Copying deleted message to trash mailbox (Automatically removes from current folder)
                folder.copyMessages(new Message[] { read_messages[i] }, trashFolder);
                break;

            }
        }

        // if not found, look through unread_messages to finalize deletion
        if (!found) {
            // fetch unread messages
            Message unread_messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (int i = 0; i < unread_messages.length; i++) {
                if (unread_messages[i].getMessageNumber() == index) {

                    found = true;

                    // Setting flag on message for deletion
                    unread_messages[i].setFlag(Flags.Flag.DELETED, true);

                    System.out.println("Deleting message: " + unread_messages[i].getSubject());

                    // Copying deleted message to trash mailbox (Automatically removes from current folder)
                    folder.copyMessages(new Message[] { unread_messages[i] }, trashFolder);

                    break;
                }
            }
        }

        // Open trash folder and finalize deletion
        if(trashFolder.isOpen()) {
            trashFolder.open(Folder.READ_ONLY);
            trashFolder.expunge();
        }

        File file = new File("src/data/" + mailbox + "/emails.json");
        JsonStreamParser parser = new JsonStreamParser(new FileReader(file));

        Gson gsonIn = new GsonBuilder().create();

        // Extracting classes from json file in a list.
        Email[] email_list = gsonIn.fromJson(parser.next(), Email[].class);

        email_list = removeElement(email_list, index);

        // Creating new email list array adjusting the size
        Email[] newEmailList;
        try {
            newEmailList = Arrays.copyOfRange(email_list, 0, email_list.length-2);
            // Updating json data file
            File jsonFile = new File("src/data/" + mailbox + "/emails.json");
            jsonFile.getParentFile().mkdirs();
            try (Writer jsonOutput = new FileWriter(jsonFile)) {
                Gson gsonOut = new GsonBuilder().setPrettyPrinting().create();
                gsonOut.toJson(newEmailList, jsonOutput);
            }
        }
        catch (Exception e) {
            // Seems to be deleting fine regardless of error
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Issue deleting file.");
            error.showAndWait();
            //e.printStackTrace();
        }

        // Delete emails html content file
        File htmlFile = new File("src/data/" + mailbox + "/email-" + index + ".html");
        FileUtils.forceDelete(htmlFile);

        LoadEmailListView.loadData(emailList, wb, mailbox, searchField);

        // Clear WebView
        wb.getEngine().loadContent("");

    }

    // Using a linked list to delete desired email locally and to reshuffle other emails.
    public static Email[] removeElement(Email[] input, int index) {

        List<Email> result = new LinkedList<>();

        for(Email item : input) {
            // add all emails NOT to be deleted to linked list
            if (item.getId() != index) {
                result.add(item);
            }

        }

        return result.toArray(input);
    }

}