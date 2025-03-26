package br.edu.ifes.mestrado.emailAPI;

import br.edu.ifes.mestrado.emailAPI.controller.DataController;
import br.edu.ifes.mestrado.emailAPI.model.Data;
import br.edu.ifes.mestrado.emailAPI.service.MeetingInviteService;

import java.time.ZonedDateTime;

public class Teste {
    public static void main(String[] args) {
        // Informações do SMTP
        String smtpHost = "smtp.gmail.com"; // Exemplo para Gmail
        String emailUser = "laboratorio902t@gmail.com";
        String emailPass = "ogwn ypsh lkdr iywz"; // Use App Password se necessário

        // Criando os dados de data usando a classe Data
        Data startData = new Data("12", "03", "2025", "14", "30");  // 12 de março de 2025 às 14:30
        Data endData = new Data("12", "03", "2025", "21", "30");    // 12 de março de 2025 às 15:30

        // Usando o DataController para converter a data para LocalDateTime
        DataController dataController = new DataController();
        ZonedDateTime startDate = dataController.Data(startData);
        ZonedDateTime endDate = dataController.Data(endData);

        // Função que envia convite para a pessoa
        MeetingInviteService inviteService = new MeetingInviteService(smtpHost, emailUser, emailPass);

        // Enviar convite para o destinatário
        inviteService.sendMeetingInvite("davidsonifes@gmail.com", "Reunião Importante", "Discussão sobre o projeto X.", startDate, endDate);
    }
}
