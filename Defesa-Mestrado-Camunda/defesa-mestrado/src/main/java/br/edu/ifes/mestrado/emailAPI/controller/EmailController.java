package br.edu.ifes.mestrado.emailAPI.controller;


import br.edu.ifes.mestrado.emailAPI.model.EmailLogin;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;

import java.util.List;

public class EmailController {
    private EmailLogin emailLogin;
    private EmailView emailView;
    private EmailSenderService emailSenderService;

    public EmailController() {
        emailLogin = new EmailLogin();
        emailView = new EmailView();
        emailSenderService = new EmailSenderService(emailLogin.imapHost, emailLogin.username, emailLogin.password);
    }

    public void sendEmail(String to, String subject, String body) {
        boolean success = emailSenderService.sendEmail(to, subject, body);
        emailView.displaySendEmailStatus(success);
    }
}
