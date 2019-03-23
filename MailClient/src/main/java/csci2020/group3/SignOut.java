package csci2020.group3;


import javafx.scene.control.Alert;

import java.io.File;

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
            deleteDir(file);
            file = new File("src/data/[Gmail]");
            deleteDir(file);

        } catch(Exception e) {
            // Sign out error exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error signing out, try again.");
            alert.showAndWait();
        }

    }

    // Deletes inputted director
    private static void deleteDir(File dir) {

        File[] folder = dir.listFiles();

        if(folder != null) {
            for(File f: folder) {
                // Deletes file or directory depending
                if(f.isDirectory()) {
                    System.out.println("deleting directory...");
                    deleteDir(f);

                } else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }
}