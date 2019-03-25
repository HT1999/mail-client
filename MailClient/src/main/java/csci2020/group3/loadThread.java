package csci2020.group3;

import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class loadThread implements Runnable {

    // initialize all relevant user and system information
    private String email_addr;
    private String pwd;
    private ListView<EmailListView.EmailList> emailList;
    private WebView wb;
    private String[] mailboxList = { "INBOX", "[Gmail]/Sent Mail", "[Gmail]/Trash", "[Gmail]/Spam", "[Gmail]/Starred"};
    private TextField searchField;
    private ProgressBar loading;

    // Thread Constructor
    public loadThread(String email_addr, String pwd, ListView<EmailListView.EmailList> emailList, WebView wb, TextField searchField, ProgressBar loading) {
        this.email_addr = email_addr;
        this.pwd = pwd;
        this.emailList = emailList;
        this.wb = wb;
        this.searchField = searchField;
        this.loading = loading;
    }

    public void run() {
        // initialize loading bar values
        final Float[] values = new Float[] {0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
        long startTime = System.nanoTime();

        // each open IMAPFolder gets a single connection to the server so concurrency is limited
        for (int i = 0; i < 5; i++) {
            StoreEmails.storeEmails(this.email_addr, this.pwd, mailboxList[i]);
            loading.setProgress(values[i]);
        }

        long endTime = System.nanoTime();
        long timeElapsed = (endTime - startTime)/1000000000;
        System.out.println("Time taken to load files: " + timeElapsed + "s");

        // loading bar complete
        loading.setProgress(1.0f);

        try {
            // Loads the users INBOX into the emailList by default
            LoadEmailListView.loadData(emailList, wb, "INBOX", searchField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
