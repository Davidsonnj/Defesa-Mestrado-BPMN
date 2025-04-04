package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarEmailDadosAlunoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        EmailController emailController = new EmailController();
        Boolean recebeuEmail = false;

        String aluno = (String) execution.getVariable("aluno");
        String emailOrientador = (String) execution.getVariable("emailOrientador");

        String subject = String.format("Dados do aluno - %s", aluno);
        System.out.println("Verificando o email do Orientador " + emailOrientador);
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
            execution.setVariable("data_defesa", data);
            execution.setVariable("hora_defesa", hora);
            execution.setVariable("local_defesa", local);
            execution.setVariable("banca_defesa", banca);

            System.out.println("Dados extraídos:");
            System.out.println("Data: " + data);
            System.out.println("Hora: " + hora);
            System.out.println("Local: " + local);
            System.out.println("Banca: " + banca);

        } else {
            execution.setVariable("recebeuEmail", false);
            System.out.println("Nenhum e-mail encontrado.");
        }
    }

    private String extrairCampo(String texto, String campo) {
        for (String linha : texto.split("\n")) {
            if (linha.trim().startsWith(campo + ":")) {
                return linha.split(":", 2)[1].trim();
            }
        }
        return "";
    }

    private List<Map<String, String>> extrairBanca(String corpo) {
        List<Map<String, String>> banca = new ArrayList<>();

        String[] linhas = corpo.split("\n");
        boolean lendoBanca = false;
        Map<String, String> membroAtual = new HashMap<>();

        for (String linha : linhas) {
            linha = linha.trim();

            if (linha.startsWith("Banca:")) {
                lendoBanca = true;
                continue;
            }

            if (lendoBanca) {
                if (linha.startsWith("Nome:")) {
                    if (!membroAtual.isEmpty()) {
                        banca.add(new HashMap<>(membroAtual));
                        membroAtual.clear();
                    }
                    membroAtual.put("nome", linha.split(":", 2)[1].trim());
                } else if (linha.startsWith("Email:")) {
                    membroAtual.put("email", linha.split(":", 2)[1].trim());
                } else if (linha.startsWith("Instituição:")) {
                    membroAtual.put("instituicao", linha.split(":", 2)[1].trim());
                } else if (linha.startsWith("Minicurrículo:")) {
                    membroAtual.put("minicurriculo", linha.split(":", 2)[1].trim());
                }
            }
        }

        if (!membroAtual.isEmpty()) {
            banca.add(membroAtual);
        }

        return banca;
    }

}