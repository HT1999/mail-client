package csci2020.group3;

import java.util.Date;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;

public class SendEmail {

    public static void sendMail(String from, String to, String pwd, String subject, String email_txt) {

        // Setting up properties using Google's smtp server
        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        // Authenticating details
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pwd);
            }
        });

        try {
            // Initializing message object
            MimeMessage msg = new MimeMessage(session);

            // Storing email addresses (multiples seperated by commas)
            InternetAddress[] address = InternetAddress.parse(to, true);

            // Filling email message with data
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(email_txt);
            msg.setHeader("XPriority", "1");
            Transport.send(msg);

            // Success console log
            System.out.println("Mail sent successfully");
        } catch (MessagingException mex) {
            // Failure to send and why
            System.out.println("Exception sending email: " + mex);
        }
    }
}

