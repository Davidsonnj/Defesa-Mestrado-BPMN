package br.edu.ifes.mestrado.emailAPI.controller;

import br.edu.ifes.mestrado.emailAPI.model.EmailLogin;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;

import java.util.List;

public class SenderEmailController {
    private EmailLogin emailLogin;
    private EmailSenderService emailSenderService;
    private EmailView emailView = new EmailView();


    public SenderEmailController() {
        emailLogin = new EmailLogin();
        emailSenderService = new EmailSenderService(emailLogin.smtpHost, emailLogin.username, emailLogin.password);
    }

    public void sendEmail(String to, String subject, String body) {
        boolean success = emailSenderService.sendEmail(to, subject, body);
        emailView.displaySendEmailStatus(success);
    }

    public void sendEmail(String to, String subject, String body, List<String> paths) {
        boolean success = emailSenderService.sendEmail(to, subject, body, paths);
        emailView.displaySendEmailStatus(success);
    }
}
