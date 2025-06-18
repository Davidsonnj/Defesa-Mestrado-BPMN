package br.edu.ifes.mestrado.emailAPI.model;

import br.edu.ifes.mestrado.camunda.model.Banca;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtrairDadosEmail {

    public static class DadosExtraidos {

        public String aluno;
        public String email;
        public String titulo;
        public String emailOrientador;
        public String dataDefesa;
        public String horaDefesa;
        public String localDefesa;
        public List<Banca> banca;


        public DadosExtraidos(String aluno, String email, String titulo, String dataDefesa, String horaDefesa, String localDefesa, List<Banca> banca) {
            this.aluno = aluno;
            this.email = email;
            this.titulo = titulo;
            this.dataDefesa = dataDefesa;
            this.horaDefesa = horaDefesa;
            this.localDefesa = localDefesa;
            this.banca = banca;

        }
    }

    public static DadosExtraidos extrairDados(String texto) {
        Pattern pattern = Pattern.compile(
                "aluno:\\s*(.*?)\\s*email:\\s*(.*?)\\s*titulo:\\s*(.*)",
                Pattern.CASE_INSENSITIVE
        );


        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            String aluno = matcher  .group(1);
            String email = matcher.group(2);
            String titulo = matcher.group(3);
            return new DadosExtraidos(aluno, email, titulo);
        } else {
            return null;
        }
    }
}
