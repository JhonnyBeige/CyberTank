package gtanks.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private final String username;
    private final String password;
    private final Properties properties;

    private static final EmailService instance = new EmailService("smtp.hostinger.com", "465", "support@cybertankz.com", "fvtpswmQjC4joCV$");

    public EmailService(String host, String port, String username, String password) {
        this.username = username;
        this.password = password;

        // Set mail properties
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host); // smtp.hostinger.com
        properties.put("mail.smtp.port", port); // 465
        properties.put("mail.smtp.ssl.trust", host); // Trust the host
        properties.put("mail.smtp.socketFactory.port", port); // Port 465 for SSL
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // Enable SSL
        properties.put("mail.smtp.starttls.enable", "true"); // Disable STARTTLS since SSL is being used
        properties.put("mail.smtp.connectiontimeout", "10000"); // 10 seconds timeout
        properties.put("mail.smtp.timeout", "10000"); // 10 seconds timeout
    }

    public static EmailService getInstance() {
        return instance;
    }

    public void sendEmail(String toEmail, String subject, String messageText) {
        // Create session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("support@cybertankz.com"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(messageText);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
