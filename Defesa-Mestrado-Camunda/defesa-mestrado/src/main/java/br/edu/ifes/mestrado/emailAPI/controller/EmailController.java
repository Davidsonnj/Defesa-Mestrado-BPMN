package br.edu.ifes.mestrado.emailAPI.controller;


import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.service.EmailService;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;

import java.util.List;

public class EmailController {
    private EmailService emailService;
    private EmailView emailView;
    private EmailSenderService emailSenderService;

    public EmailController(EmailService emailService, EmailSenderService emailSenderService, EmailView emailView) {
        this.emailService = emailService;
        this.emailView = emailView;
        this.emailSenderService = emailSenderService;
    }

    public void showEmails(String subjectFilter, String bodyFilter, String senderFilter) {
        List<Email> emails = emailService.fetchEmails(subjectFilter, bodyFilter, senderFilter);
        emailView.displayEmails(emails);
    }

    public void sendEmail(String to, String subject, String body) {
        boolean success = emailSenderService.sendEmail(to, subject, body);
        emailView.displaySendEmailStatus(success);
    }
}
