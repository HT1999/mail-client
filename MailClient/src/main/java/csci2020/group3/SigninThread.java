package csci2020.group3;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class SigninThread implements Runnable {

    private Button[] buttons;
    private MenuItem signinMenu;
    private MenuItem signoutMenu;

    // Thread Constructor
    public SigninThread(Button[] buttons, MenuItem signinMenu, MenuItem signoutMenu){
        this.buttons = buttons;
        this.signinMenu = signinMenu;
        this.signoutMenu = signoutMenu;
    }

    public void run() {
        Platform.runLater(() -> {
            SignIn newSignIn = new SignIn(buttons, signinMenu, signoutMenu);
            try {
                newSignIn.signInButtonClicked();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
