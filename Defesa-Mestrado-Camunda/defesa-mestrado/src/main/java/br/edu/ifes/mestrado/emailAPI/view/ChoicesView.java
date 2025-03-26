package br.edu.ifes.mestrado.emailAPI.view;

import java.util.Scanner;

public class ChoicesView {
    public int showMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("📧 Bem-vindo ao EmailReaderMVC");
        System.out.println("1 - Listar e-mails");
        System.out.println("2 - Enviar e-mail");
        System.out.println("Escolha uma opção:");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        return opcao;

    }


    public String[] getFilterOptions() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("📧 Bem-vindo ao EmailReaderMVC");
        System.out.println("1 - Filtrar Email");
        System.out.println("2 - Não Filtrar");
        System.out.print("Escolha uma opção: ");
        int opcaoFilter = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        String subjectFilter = null;
        String bodyFilter = null;
        String senderFilter = null;

        if (opcaoFilter == 1) {
            // Solicita filtros de remetente, assunto e corpo do e-mail
            System.out.print("Digite o filtro para o remetente (deixe em branco para nenhum filtro): ");
            senderFilter = scanner.nextLine();

            System.out.print("Digite o filtro para o assunto (deixe em branco para nenhum filtro): ");
            subjectFilter = scanner.nextLine();

            System.out.print("Digite o filtro para o corpo do e-mail (deixe em branco para nenhum filtro): ");
            bodyFilter = scanner.nextLine();
        }

        return new String[]{subjectFilter, bodyFilter, senderFilter};
    }
}

