package br.edu.ifes.mestrado.emailAPI;

import br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosEmail;

import static br.edu.ifes.mestrado.emailAPI.model.ExtrairDadosEmail.extrairDados;

public class teste {
    public static void main(String[] args) {
        String texto = "aluno: Carlos Eduardo Silva   email: carlos.silva@email.com   titulo: Análise dos Impactos das Redes Sociais na Educação";

        ExtrairDadosEmail.DadosExtraidos dados = extrairDados(texto);

        if (dados != null) {
            System.out.println("Aluno: " + dados.aluno);
            System.out.println("Email: " + dados.email);
            System.out.println("Titulo: " + dados.titulo);
        } else {
            System.out.println("Nenhum dado encontrado!");
        }
    }

}
