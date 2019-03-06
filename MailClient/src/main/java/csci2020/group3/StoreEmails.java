package csci2020.group3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class StoreEmails {
    private static Folder sent;
    static FileWriter writeFile;
    static FileWriter writeFileCurrent;
    static FileWriter writeFileTest;

    static {
        try {
            File file = new File("/MailClient/Data/emails.txt");
            file.getParentFile().mkdirs();
            writeFile = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ** Currently only handles multipart emails **
    public static void ReadSentMail(String email_addr, String pwd) {

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {

            // Initializing session and store var
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email_addr, pwd);

            // Loads specified folder and prints total number of emails inside
            sent = store.getFolder("INBOX");
            System.out.println("Total # of emails: " + sent.getMessageCount());

            // Opening store folder
            sent.open(Folder.READ_ONLY);
            Message messages[] = sent.search(new FlagTerm(new Flags(Flag.SEEN), true));

            // Setting up FetchProfile
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            sent.fetch(messages, fp);

            // Creating an array of Emails to store each email
            Email[] emails = new Email[sent.getMessageCount()];

            try {
                printFolder(messages, emails);
                sent.close(true);
                store.close();
            } catch (Exception ex) {
                System.out.println("Reading mail exception");
                ex.printStackTrace();
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static int count;
    // Outputs all emails from desired gmail folder
    public static void printFolder(Message[] msgs, Email[] emails) throws Exception {

        // Creating an array of Emails to store each email


        count = msgs.length;
        for (int i = msgs.length - 1; i >= 0; i--) {

            // Creating Email Class to store key components
            Email email = new Email();

            System.out.println("Email #" + (i + 1) + ":");
            writeFile.write("Email #" + (i + 1) + ":");
            email.setId(i+1);

            printEmail(msgs[i], email);

            // Copying current email to emails list
            emails[count-1] = email;

            count--;
        }
        writeFile.close();

        // Creating json data file with all emails
        File jsonFile = new File("/MailClient/Data/emails.json");
        jsonFile.getParentFile().mkdirs();
        try (Writer jsonOutput = new FileWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(emails, jsonOutput);
        }
    }

    // Outputs Email
    public static void printEmail(Message message, Email email) throws Exception {
        Address[] a;


        // Getting email author
        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("FROM: " + a[j].toString());

                email.setFrom(a[j].toString());
                //writeFile.write("FROM: " + a[j].toString());
            }
        }
        // Getting email recipient
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("TO: " + a[j].toString());
                //writeFile.write("TO: " + a[j].toString());
            }
        }
        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
        String content = message.getContent().toString();

        email.setSubject(message.getSubject());
        email.setDate(message.getReceivedDate());

        //writeFile.write("Subject: " + subject);
        //writeFile.write("Received Date: " + receivedDate.toString());
        //writeFile.write("Content: " + content);

        System.out.println("Subject: " + subject);
        System.out.println("Received Date: " + receivedDate.toString());
        System.out.println("Content: " + content);
        getContent(message, email);

        //writeFile.close();
    }

    public static void getContent(Message msg, Email email) {
        try {

            String contentType = msg.getContentType();

            writeFile.write("Content Type: " + contentType);

            System.out.println("Content Type: " + contentType);

            // Checking if mimetype is a multipart
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {

                    String temp = mp.getBodyPart(i).getContentType();
                    System.out.println(temp);

                    if (mp.getBodyPart(i).isMimeType("text/html")) {
                        System.out.println("\n ---------- \n");

                        System.out.println(mp.getBodyPart(i).getContentType());
                        writeEmail(mp.getBodyPart(i), email);
                    }

//                    System.out.println("\n !! NEW CONTENT !! \n");
//
//                    System.out.println(mp.getBodyPart(i).getContentType());
//
//                    writeEmail(mp.getBodyPart(i));
                }
            }

            //writeFile.close();

            // Condition to handle text/plain emails
            else if (msg.isMimeType("text/plain")) {

                // Creating text file to store current text content
                try {
                    // Saving text as html for now*****
                    File curr_file = new File("/MailClient/Data/email-" + count + ".html");
                    curr_file.getParentFile().mkdirs();
                    writeFileCurrent = new FileWriter(curr_file);
                    email.setContentPath("/MailClient/Data/email-" + count + ".txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String tp = msg.getContent().toString();

                writeFile.write(tp);

                writeFileCurrent.write(tp);

                System.out.println(tp);
            }

            writeFileCurrent.close();
            //

            // getContent Exception
        } catch (Exception ex) {
            System.out.println("getContent exception");
            ex.printStackTrace();
        }
    }

    public static void writeEmail(Part p, Email email) throws Exception {

        // Creating html file to store current html content
        try {
            File curr_file = new File("/MailClient/Data/email-" + count + ".html");
            curr_file.getParentFile().mkdirs();
            writeFileCurrent = new FileWriter(curr_file);
            email.setContentPath("/MailClient/Data/email-" + count + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = p.getInputStream();

        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        int w;
        System.out.println("Message: ");
        while ((w = is.read()) != -1) {
            //FileWriter writeFile = new FileWriter("emails.txt");
            writeFile.write(w);

            writeFileCurrent.write(w);



            //writeFile.close();
           // writeFile.close();
            System.out.write(w);
        }
    }

}
