package csci2020.group3;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
//import javax.swing.*;

import static csci2020.group3.Controller.attach_field;
import static csci2020.group3.Controller.attach_path;

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

            // Initial email info
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Creating multipart email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(email_txt);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // If an attachment is added in email form
            if (attach_path != null) {
                // Adding attached file to email
                messageBodyPart = new MimeBodyPart();
                javax.activation.DataSource source = new FileDataSource(attach_path);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attach_field.getText());
                multipart.addBodyPart(messageBodyPart);
            }

            msg.setContent(multipart);

            Transport.send(msg);

            // Successfully Sent Email
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Email Sent", ButtonType.OK);
            alert.showAndWait();

            // updating attach_field after email sent
            //attach_field.setText("");

            // Success console log
            System.out.println("Mail sent successfully");
        } catch (MessagingException mex) {
            // Failure to send and why
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failure sending email: " + mex);
            System.out.println("Exception sending email: " + mex);
            errorAlert.showAndWait();
        }
    }

}

