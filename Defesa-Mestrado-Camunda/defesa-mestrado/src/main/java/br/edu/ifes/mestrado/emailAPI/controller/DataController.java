package br.edu.ifes.mestrado.emailAPI.controller;

import br.edu.ifes.mestrado.emailAPI.model.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DataController {

    public ZonedDateTime Data(Data data) {
        // Criando um LocalDateTime a partir dos valores da classe Data
        LocalDateTime localDateTime = LocalDateTime.of(
                Integer.parseInt(data.getAno()),
                Integer.parseInt(data.getMes()),
                Integer.parseInt(data.getDia()),
                Integer.parseInt(data.getHora()),
                Integer.parseInt(data.getMinuto())
        );

        // Convertendo para um ZonedDateTime no fuso horário correto (ex: "America/Sao_Paulo")
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");  // Ajuste o fuso horário conforme necessário
        return localDateTime.atZone(zoneId);
    }
}
