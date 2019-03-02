package csci2020.group3;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";

    private String email;
    private String password;

    // Default Constructor
    Preferences() {
       this.email = null;
       this.password = null;
   }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        // Currently being stored in plaintext
        // Had trouble working with a hashed password
        //this.password = DigestUtils.shaHex(password);
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void initConfig() {
        //Preferences preference = new Preferences();
        Gson gson = new Gson();
        try {
            // Writing this preference in json format
            Writer writer = new FileWriter(CONFIG_FILE);
            gson.toJson(this, writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reads current preference object reference
    public static Preferences getPreferences() {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            preferences = gson.fromJson(reader, Preferences.class);

        } catch (FileNotFoundException e) {
            // Creates config file if not found
            //this.initConfig();
            e.printStackTrace();
        }
        return preferences;
    }

}
