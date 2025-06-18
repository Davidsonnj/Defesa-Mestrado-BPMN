package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaDadosDetalhados;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.database.dao.implementations.EmailDAO;
import br.edu.ifes.mestrado.emailAPI.controller.FuncoesEmail;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BuscarEmailDadosAlunoDelegate implements JavaDelegate {

    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private PerguntaDadosDetalhados perguntaDadosDetalhados;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            Boolean recebeuEmail = false;

            String aluno = (String) execution.getVariable("aluno");
            String emailOrientador = (String) execution.getVariable("emailOrientador");

            List<Email> emails = emailDAO.findAll();
            System.out.println("Buscando dados detalhados do aluno");

            if (emails != null && !emails.isEmpty()) {

                for (Email email : emails) {

                    Map.Entry<String, String> resultado = FuncoesEmail.tratarEmailSender(email);
                    String emailOrientadorBD =  resultado.getValue();

                    if(email.getStatus().equals("DADOS_DETALHADOS_DEFESA")
                            && emailOrientadorBD.equals(emailOrientador)) {

                        String resposta = perguntaDadosDetalhados.takeQuestion(email.getBody());
                        System.out.println("--- Resposta da IA ---");
                        System.out.println(resposta);
                        System.out.println("--------------------");

                        String data = extrairCampo(resposta, "Data");
                        String hora = extrairCampo(resposta, "Hora");
                        String local = extrairCampo(resposta, "Local");
                        List<Banca> banca = extrairBanca(resposta);

                        execution.setVariable("dataDefesa", data);
                        execution.setVariable("horaDefesa", hora);
                        execution.setVariable("localDefesa", local);
                        execution.setVariable("bancaDefesa", banca);

                        System.out.println("Dados extraídos:");
                        System.out.println("Data: " + data);
                        System.out.println("Hora: " + hora);
                        System.out.println("Local: " + local);
                        System.out.println("Banca: " + banca);

                        email.setStatus("PROCESSADO");
                        emailDAO.update(email);

                        recebeuEmail = true;
                        execution.setVariable("recebeuEmail", recebeuEmail);

                    } else if(email.getStatus().equals("DADOS_DETALHADOS_DEFESA_INCORRETOS")){
                        // Enviar um email para orientador pedindo para enviar os dados corretos
                    }
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
        String[] linhas = texto.split("\n");
        for (String linha : linhas) {
            String prefixo = "- " + campo + ":";
            if (linha.trim().startsWith(prefixo)) {
                return linha.substring(linha.indexOf(":") + 1).trim();
            }
        }
        return "";
    }


    private List<Banca> extrairBanca(String corpo) {
        List<Banca> banca = new ArrayList<>();

        Pattern bancaPattern = Pattern.compile(
                "- Nome:\\s*(.*?)\\n" +
                        "- Email:\\s*(.*?)\\n" +
                        "- Instituição:\\s*(.*?)\\n" +
                        "- Minicurrículo:\\s*(.*?)(?=\\n\\n|- Nome:|$)", Pattern.DOTALL);

        Matcher matcher = bancaPattern.matcher(corpo);

        while (matcher.find()) {
            String nome = matcher.group(1).trim();
            String email = matcher.group(2).trim();
            String instituicao = matcher.group(3).trim();
            String minicurriculo = matcher.group(4).trim();
            Banca membro = new Banca(nome, email, instituicao, minicurriculo);
            banca.add(membro);
        }

        return banca;
    }
}