package csci2020.group3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {

        File file = new File("/MailClient/Data/emails.json");
        JsonStreamParser parser = new JsonStreamParser(new FileReader(file));

        Gson gson = new GsonBuilder().create();

        // Extracting classes from json file in a list.
        Email[] email_list = gson.fromJson(parser.next(), Email[].class);

        for (int i = email_list.length-1; i >= 0; i--) {
            System.out.println("\n" + (i+1) + ": " + email_list[i].getSubject());
        }
    }

}
