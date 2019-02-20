package csci2020.group3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

public class SendEmail {

    public static void sendMail(String from, String to, String pwd, String subject, String email_txt) {

        //Setting up configurations for the email connection to the Google SMTP server using TLS
        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        //Establishing a session with required user details
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
            System.out.println("Mail has been sent successfully");
        } catch (MessagingException mex) {
            // Failure to send and why
            System.out.println("Unable to send an email" + mex);
        }
    }
}

