package br.edu.ifes.mestrado.emailAPI.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

public class EmailSenderService {
    private String host;
    private String username;
    private String password;

    public EmailSenderService(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public boolean sendEmail(String to, String subject, String body) {
        try {
            // Criando a sessão de e-mail
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587"); // Porta SMTP comum para envio
            properties.put("mail.smtp.ssl.trust", host);

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Criando o e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Enviando o e-mail
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean sendEmail(String to, String subject, String body, List<String> attachmentPaths) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.trust", host);

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (String filePath : attachmentPaths) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(filePath);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
