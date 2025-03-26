package br.edu.ifes.mestrado.emailAPI;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class EmailUtils {

    // Método para obter o Message-ID
    public static String getMessageId(Message message) {
        try {
            if (message instanceof MimeMessage) {
                return ((MimeMessage) message).getMessageID();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obter o UID do e-mail via IMAP
    public static long getEmailUID(Folder folder, Message message) {
        try {
            if (folder instanceof UIDFolder) {
                return ((UIDFolder) folder).getUID(message);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
