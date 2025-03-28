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

        EmailController emailController = new EmailController();

        Scanner scanner = new Scanner(System.in);

        String to = "davidsonifes@gmail.com";
        String subject = "Solicitação de agendamento de defesa";
        String body = "Solicito age";

        emailController.sendEmail(to, subject, body);
    }
}
