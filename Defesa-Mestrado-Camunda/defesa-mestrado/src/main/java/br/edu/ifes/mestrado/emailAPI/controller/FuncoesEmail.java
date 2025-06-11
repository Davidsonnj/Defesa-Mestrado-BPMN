package br.edu.ifes.mestrado.emailAPI.controller;

import br.edu.ifes.mestrado.emailAPI.model.Email;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuncoesEmail {
    public static Map.Entry<String, String> tratarEmailSender(Email email) {
        String remetente = email.getSender();
        String regex = "^(.*?)\\s<([^<>@\\s]+@[^<>@\\s]+)>";
        Matcher matcher = Pattern.compile(regex).matcher(remetente);

        if (matcher.find()) {
            String nome = matcher.group(1).trim();
            String endereco = matcher.group(2).trim();
            return Map.entry(nome, endereco);
        } else {
            return Map.entry(remetente, null);
        }
    }

}
