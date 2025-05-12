package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.emailAPI.model.Email;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmailService {
    private String host;
    private String username;
    private String password;

    public EmailService(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public List<Email> fetchEmails(String subjectFilter, String bodyFilter, String senderFilter) {
        List<Email> emails = new ArrayList<>();

        try {
            Session session = EmailSessionFactory.createSession(host);
            Store store = session.getStore("imap");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            for (Message message : inbox.getMessages()) {
                String subject = message.getSubject();
                String sender = message.getFrom()[0].toString();
                String body = getTextFromMessage(message);

                // Aplicando os filtros
                boolean matchesSubject = (subjectFilter == null || subject.toLowerCase().equalsIgnoreCase(subjectFilter.toLowerCase()));
                boolean matchesBody = (bodyFilter == null || body.toLowerCase().contains(bodyFilter.toLowerCase()));
                boolean matchesSender = (senderFilter == null || sender.toLowerCase().contains(senderFilter.toLowerCase()));

                if (matchesSubject && matchesBody && matchesSender) {
                    List<String> attachmentPaths = new ArrayList<>();
                    if (message.getContent() instanceof MimeMultipart) {
                        attachmentPaths = extractAttachments((MimeMultipart) message.getContent());
                    }
                    emails.add(new Email(subject, sender, message.getSentDate(), body, attachmentPaths));
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }



    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) message.getContent());
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart part = mimeMultipart.getBodyPart(i);
            if (part.isMimeType("text/plain")) {
                text.append((String) part.getContent());
            } else if (part.isMimeType("text/html")) {
                text.append((String) part.getContent());
            }
        }
        return text.toString();
    }

    private List<String> extractAttachments(MimeMultipart multipart) throws Exception {
        String outputDir = "/home/davidson/Desktop/Defesa-Mestrado-BPMN/Defesa-Mestrado-Camunda/anexos/";
        List<String> savedFiles = new ArrayList<>();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) && part.getFileName() != null) {
                String fileName = part.getFileName();
                File file = new File(outputDir + fileName);
                file.getParentFile().mkdirs();

                try (InputStream is = part.getInputStream();
                     FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    savedFiles.add(file.getAbsolutePath());
                    System.out.println("✅ Anexo salvo: " + file.getAbsolutePath());
                }
            }

            // Se multipart aninhado
            if (part.getContent() instanceof MimeMultipart) {
                savedFiles.addAll(extractAttachments((MimeMultipart) part.getContent()));
            }
        }

        return savedFiles;
    }

}
