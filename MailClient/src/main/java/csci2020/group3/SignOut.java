package csci2020.group3;


import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SignOut {

    // Clears users data locally and closes the application
    public void signOut() {

        try {
            // Clearing config.txt
            File file = new File(Preferences.CONFIG_FILE);
            file.delete();

            // Manually making sure Inbox emails.json is deleted
            File inbox_file = new File("/data/INBOX/emails.json");
            inbox_file.delete();

            // Clearing the data files
            file = new File("src/data/INBOX");
            Thread.sleep(100);
            deleteDir(file);
            file = new File("src/data/[Gmail]");
            Thread.sleep(100);
            deleteDir(file);

        } catch(Exception e) {
            // Sign out error exception
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error signing out, try again.");
            alert.showAndWait();
        }

    }

    // Deletes inputted director
    private static void deleteDir(File dir) throws IOException {

        File[] folder = dir.listFiles();

        if(folder != null) {
            for(File f: folder) {
                // Deletes file or directory depending
                if(f.isDirectory()) {
                    deleteDir(f);
                } else {
                    try {
                        FileUtils.forceDelete(f);
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }
        dir.delete();
    }
}