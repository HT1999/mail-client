package csci2020.group3;

import javafx.scene.control.Button;
import java.io.File;

// Enables/Disables the main windows buttons for un-authorized users

public class ButtonState {
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
}
