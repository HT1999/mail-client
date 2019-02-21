package csci2020.group3;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

public class StoreEmails {
    private static Folder sent;

    // ** Currently only handles multipart emails **
    public static void ReadSentMail(String email, String pwd) {

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {

            // Initializing session and store var
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, pwd);

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

            try {
                printFolder(messages);
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

    // Outputs all emails from desired gmail folder
    public static void printFolder(Message[] msgs) throws Exception {
        for (int i = msgs.length - 1; i > 0; i--) {
            System.out.println("Email #" + (i + 1) + ":");
            printEmail(msgs[i]);
        }
    }

    // Outputs Email
    public static void printEmail(Message message) throws Exception {
        Address[] a;

        // Getting email author
        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("FROM: " + a[j].toString());
            }
        }
        // Getting email recipient
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("TO: " + a[j].toString());
            }
        }
        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
        String content = message.getContent().toString();
        System.out.println("Subject: " + subject);
        System.out.println("Received Date: " + receivedDate.toString());
        System.out.println("Content: " + content);
        getContent(message);
    }

    public static void getContent(Message msg) {
        try {
            String contentType = msg.getContentType();
            System.out.println("Content Type: " + contentType);

            // Checking if mimetype is a multipart
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    writeEmail(mp.getBodyPart(i));
                }
            }

            /* Condition to handle text/plain emails
            else if (msg.isMimeType("text/plain")) {
                String tp = msg.getContent().toString();
                System.out.println(tp);
            }
            */

            // getContent Exception
        } catch (Exception ex) {
            System.out.println("getContent exception");
            ex.printStackTrace();
        }
    }

    public static void writeEmail(Part p) throws Exception {

        InputStream is = p.getInputStream();

        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        int w;
        System.out.println("Message: ");
        while ((w = is.read()) != -1) {
            System.out.write(w);
        }
    }
}
