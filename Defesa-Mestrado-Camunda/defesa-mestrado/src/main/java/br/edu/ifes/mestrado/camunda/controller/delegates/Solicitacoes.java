package br.edu.ifes.mestrado.camunda.controller.delegates;

import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.service.EmailService;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;

import java.util.Scanner;

public class Solicitacoes implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String imapHost = "imap.gmail.com";
        String smtpHost = "smtp.gmail.com"; // Para envio
        String username = "laboratorio902t@gmail.com";
        String password = "ogwn ypsh lkdr iywz"; // Use senha de app para segurança

        EmailService emailService = new EmailService(imapHost, username, password);
        EmailSenderService emailSenderService = new EmailSenderService(smtpHost, username, password);
        EmailView emailView = new EmailView();
        EmailController emailController = new EmailController(emailService, emailSenderService, emailView);

        Scanner scanner = new Scanner(System.in);

        String to = "davidsonifes@gmail.com";
        String subject = "Solicitação de agendamento de defesa";
        String body = "Solicito age";

        emailController.sendEmail(to, subject, body);
    }
}
