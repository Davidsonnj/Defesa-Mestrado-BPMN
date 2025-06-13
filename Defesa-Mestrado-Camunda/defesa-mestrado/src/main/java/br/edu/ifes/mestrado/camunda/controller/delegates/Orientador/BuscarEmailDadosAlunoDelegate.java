package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.MarkEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuscarEmailDadosAlunoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if(execution.hasVariable("verificaEmail")) {
            EmailController emailController = new EmailController();
            MarkEmail markEmail = new MarkEmail();
            Boolean recebeuEmail = false;

            String aluno = (String) execution.getVariable("aluno");
            String emailOrientador = (String) execution.getVariable("emailOrientador");

            String subject = String.format("Dados do aluno - %s", aluno);
            System.out.println("Verificando se existem emails do Orientador " + emailOrientador + "...");
            List<Email> emails = emailController.emails(subject, null, emailOrientador);

            if (emails != null && !emails.isEmpty()) {
                recebeuEmail = true;

                Email resposta = emails.get(0);
                String corpo = resposta.getBody();

                String data = extrairCampo(corpo, "Data");
                String hora = extrairCampo(corpo, "Hora");
                String local = extrairCampo(corpo, "Local");

                List<Map<String, String>> banca = extrairBanca(corpo);

                execution.setVariable("recebeuEmail", true);
                execution.setVariable("dataDefesa", data);
                execution.setVariable("horaDefesa", hora);
                execution.setVariable("localDefesa", local);
                execution.setVariable("bancaDefesa", banca);

                System.out.println("Dados extraídos:");
                System.out.println("Data: " + data);
                System.out.println("Hora: " + hora);
                System.out.println("Local: " + local);
                System.out.println("Banca: " + banca);

                for(Email email : emails) {
                    markEmail.markEmailAsRead(email.getUid());
                }

            } else {
                execution.setVariable("recebeuEmail", false);
                System.out.println("Nenhum e-mail encontrado.");
            }
        } else{
            execution.setVariable("verificaEmail", 1);
            System.out.println("Passou ao verificar o email do Orientador.");
        }
    }

    private String extrairCampo(String texto, String campo) {
        Pattern pattern = Pattern.compile(campo + ":\\s*(.*?)(?=\\s+[A-Z][a-z]+:|$)");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private List<Map<String, String>> extrairBanca(String corpo) {
        List<Map<String, String>> banca = new ArrayList<>();

        Pattern bancaPattern = Pattern.compile("Nome:\\s*(.*?)\\s+Email:\\s*(.*?)\\s+Instituição:\\s*(.*?)\\s+Minicurrículo:\\s*(.*?)(?=\\s+Nome:|$)");
        Matcher matcher = bancaPattern.matcher(corpo);

        while (matcher.find()) {
            Map<String, String> membro = new HashMap<>();
            membro.put("nome", matcher.group(1).trim());
            membro.put("email", matcher.group(2).trim());
            membro.put("instituicao", matcher.group(3).trim());
            membro.put("minicurriculo", matcher.group(4).trim());
            banca.add(membro);
        }

        return banca;
    }


}