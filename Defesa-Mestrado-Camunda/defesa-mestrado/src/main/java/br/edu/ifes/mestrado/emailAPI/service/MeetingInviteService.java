package br.edu.ifes.mestrado.emailAPI.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

public class MeetingInviteService {
    private String host;
    private String username;
    private String password;

    public MeetingInviteService(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void sendMeetingInvite(String recipient, String subject, String meetingDetails, ZonedDateTime startDate, ZonedDateTime endDate) {
        try {
            // Convertendo as datas de BRT (fuso horário local) para UTC
            ZonedDateTime startUtc = startDate.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime endUtc = endDate.withZoneSameInstant(ZoneOffset.UTC);

            // Configuração do servidor SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587"); // Porta SMTP

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Criando a mensagem de email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Formatar datas no formato iCalendar (UTC)
            DateTimeFormatter icsDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
            String startUtcFormatted = startUtc.format(icsDateFormat);
            String endUtcFormatted = endUtc.format(icsDateFormat);
            String timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(icsDateFormat); // Data atual em UTC como timestamp

            // Criando o conteúdo do convite iCalendar (.ics)
            String icsContent =
                    "BEGIN:VCALENDAR\n" +
                            "PRODID:-//My Company//Meeting Invite//EN\n" +
                            "VERSION:2.0\n" +
                            "CALSCALE:GREGORIAN\n" +
                            "METHOD:REQUEST\n" +
                            "BEGIN:VEVENT\n" +
                            "UID:" + System.currentTimeMillis() + "@mycompany.com\n" +
                            "DTSTAMP:" + timestamp + "\n" +
                            "DTSTART:" + startUtcFormatted + "\n" +
                            "DTEND:" + endUtcFormatted + "\n" +
                            "SUMMARY:" + subject + "\n" +
                            "DESCRIPTION:" + meetingDetails + "\n" +
                            "LOCATION:Google Meet ou Presencial\n" +
                            "STATUS:CONFIRMED\n" +
                            "ORGANIZER;CN=Organizador:MAILTO:" + username + "\n" +
                            "ATTENDEE;CN=Participante;RSVP=TRUE:MAILTO:" + recipient + "\n" +
                            "SEQUENCE:0\n" +
                            "BEGIN:VALARM\n" +
                            "TRIGGER:-PT10M\n" +
                            "ACTION:DISPLAY\n" +
                            "DESCRIPTION:Lembrete da reunião\n" +
                            "END:VALARM\n" +
                            "END:VEVENT\n" +
                            "END:VCALENDAR";

            // Criando a parte do convite como anexo .ics
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(icsContent, "text/calendar; charset=UTF-8; method=REQUEST")));
            attachmentPart.setFileName("convite.ics");

            // Criando a parte do convite inline (para que o email seja tratado como convite)
            MimeBodyPart calendarPart = new MimeBodyPart();
            calendarPart.setDataHandler(new DataHandler(new ByteArrayDataSource(icsContent, "text/calendar; charset=UTF-8; method=REQUEST")));
            calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            calendarPart.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");

            // Criando a parte do texto do e-mail
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(
                    "Você está convidado para uma reunião.\n\n" +
                            "Detalhes:\n" + meetingDetails + "\n\n" +
                            "Link do Google Meet: https://meet.google.com/xyz\n\n" +
                            "Por favor, confirme sua presença respondendo essa somente com a mensagem 'Aceito' ou 'Recuso'."
            );

            // Montando o e-mail com o anexo e o conteúdo inline
            Multipart multipart = new MimeMultipart("mixed");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            multipart.addBodyPart(calendarPart); // Enviar o calendário como parte inline para garantir a leitura pelo cliente de e-mail

            message.setContent(multipart);

            // Enviando e-mail
            Transport.send(message);
            System.out.println("Convite enviado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
