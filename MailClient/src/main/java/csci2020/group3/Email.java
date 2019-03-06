package csci2020.group3;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

// Class to store key segments of the email
public class Email {

    public String EMAIL_FILE = "emails";
    public String EMAIL_EXTENSION = ".txt";

    private String from;
    private int id;
    private String subject;
    private Date date;
    private String content_path;

    // Empty Constructor
    Email() {
        this.from = null;
        this.id = -1;
        this.subject = null;
        this.content_path = null;
    }

    // Constructor
    Email (String from, int id, String subject, String content_path) {
        this.from = from;
        this.id = id;
        this.subject = subject;
        this.content_path = content_path;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContentPath() {
        return content_path;
    }

    public void setContentPath(String cp) {
        this.content_path = cp;
    }

    public void initConfig() {
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(EMAIL_FILE + EMAIL_EXTENSION);
            gson.toJson(this, writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
