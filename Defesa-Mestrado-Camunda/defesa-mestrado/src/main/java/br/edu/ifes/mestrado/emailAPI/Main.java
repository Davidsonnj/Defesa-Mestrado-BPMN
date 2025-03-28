package br.edu.ifes.mestrado.emailAPI;

import br.edu.ifes.mestrado.emailAPI.service.EmailService;
import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import br.edu.ifes.mestrado.emailAPI.view.ChoicesView;
import br.edu.ifes.mestrado.emailAPI.view.EmailView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EmailController emailController = new EmailController();

        Scanner scanner = new Scanner(System.in);

        ChoicesView choicesView = new ChoicesView();


        int opcao = choicesView.showMenu();

        if (opcao == 1) {

            String[] filters = choicesView.getFilterOptions();
            String subjectFilter = filters[0];
            String bodyFilter = filters[1];
            String senderFilter = filters[2];

            // Exibe os e-mails com os filtros aplicados
            emailController.showEmails(subjectFilter, bodyFilter, senderFilter);
        } else if (opcao == 2) {
            System.out.print("Para: ");
            String to = scanner.nextLine();
            System.out.print("Assunto: ");
            String subject = scanner.nextLine();
            System.out.print("Mensagem: ");
            String body = scanner.nextLine();

            emailController.sendEmail(to, subject, body);
        } else {
            System.out.println("❌ Opção inválida.");
        }

        scanner.close();
    }
}


