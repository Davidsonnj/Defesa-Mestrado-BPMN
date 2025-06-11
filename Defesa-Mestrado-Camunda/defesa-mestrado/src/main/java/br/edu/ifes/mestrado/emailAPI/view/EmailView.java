package br.edu.ifes.mestrado.emailAPI.view;

import br.edu.ifes.mestrado.emailAPI.model.Email;
import java.util.List;

public class EmailView {
    public void displayEmails(List<Email> emails) {
        System.out.println("Número de e-mails: " + emails.size());
        for (Email email : emails) {
            System.out.println("Assunto: " + email.getSubject());
            System.out.println("De: " + email.getSender());
            System.out.println("Data: " + email.getDate());
            System.out.println("Corpo: " + email.getBody());
            System.out.println("Status: " + email.getStatus());
            System.out.println("---------------------------------");
        }
    }

    public void displaySendEmailStatus(boolean success) {
        if (success) {
            System.out.println("✅ E-mail enviado com sucesso!");
        } else {
            System.out.println("❌ Falha ao enviar o e-mail.");
        }
    }
}

