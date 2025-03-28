package br.edu.ifes.mestrado.emailAPI.controller;


import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.model.EmailLogin;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.service.EmailService;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;

import java.util.List;

public class EmailController {
    private EmailLogin emailLogin;
    private EmailService emailService;
    private EmailView emailView;
    private EmailSenderService emailSenderService;

    public EmailController() {
        emailLogin = new EmailLogin();
        emailService = new EmailService(emailLogin.imapHost, emailLogin.username, emailLogin.password);
        emailView = new EmailView();
        emailSenderService = new EmailSenderService(emailLogin.imapHost, emailLogin.username, emailLogin.password);
    }

    public boolean showEmails(String subjectFilter, String bodyFilter, String senderFilter) {
        List<Email> emails = emailService.fetchEmails(subjectFilter, bodyFilter, senderFilter);
        emailView.displayEmails(emails);
        return !emails.isEmpty();
    }

    public void sendEmail(String to, String subject, String body) {
        boolean success = emailSenderService.sendEmail(to, subject, body);
        emailView.displaySendEmailStatus(success);
    }
}
