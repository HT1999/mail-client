package csci2020.group3;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import java.io.File;

public class ButtonState {

    // Enables/Disables the main windows buttons for un-authorized users
    public void setButtons(Button[] buttons) {
        // check if config.txt file exists, returns true/false
        File file = new File("config.txt");
        if(file.exists() && !file.isDirectory()) {
            // Enables buttons
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setDisable(false);
            }
        }
        else {
            // Disables buttons
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setDisable(true);
            }
        }
    }

    public void setMenuBarItems(MenuItem signin, MenuItem signout) {

        // Hides/Enables MenuItem to avoid user confusion (disables sign-in/signout)
        File file = new File("config.txt");
        if(file.exists() && !file.isDirectory()) {
            // Enables
            signin.setVisible(false);
            signout.setVisible(true);
        }
        else {
            // Hides
            signin.setVisible(true);
            signout.setVisible(false);
        }

    }
}
