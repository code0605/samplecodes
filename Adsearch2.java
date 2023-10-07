import javax.mail.*;
import javax.mail.internet.*;

public class SendEmailViaOutlook {
    public static void main(String[] args) {
        // Set your Outlook email account settings
        String username = "your-email@example.com"; // Replace with your email address
        String password = "your-password"; // Replace with your email password

        // Set the SMTP server and port for Outlook
        String host = "smtp.office365.com"; // Replace with your SMTP server address
        int port = 587; // Use port 587 for TLS encryption

        // Set the properties for the JavaMail session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Create a JavaMail session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Set the sender's email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com")); // Replace with the recipient's email address
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Java through Outlook.");

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
