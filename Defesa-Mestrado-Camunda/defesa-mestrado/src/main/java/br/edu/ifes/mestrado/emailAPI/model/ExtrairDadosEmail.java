package br.edu.ifes.mestrado.emailAPI.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtrairDadosEmail {

    public static class DadosExtraidos {

        public String aluno;
        public String email;
        public String titulo;

        public DadosExtraidos(String aluno, String email, String titulo) {
            this.aluno = aluno;
            this.email = email;
            this.titulo = titulo;
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
