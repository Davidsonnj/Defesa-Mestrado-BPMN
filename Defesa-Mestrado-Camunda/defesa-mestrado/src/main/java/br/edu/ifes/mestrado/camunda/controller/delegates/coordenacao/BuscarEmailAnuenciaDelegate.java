package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import br.edu.ifes.mestrado.emailAPI.service.MarkEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuscarEmailAnuenciaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        if(execution.hasVariable("verificaEmail")) {
            EmailController emailController = new EmailController();
            MarkEmail markEmail = new MarkEmail();
            Boolean recebeuEmail = false;

            String email = "davidsoncsantos45@gmail.com";
            String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");

            String subject = String.format("Anuencia de defesa - %s", titulo_trabalho);
            System.out.println(subject);

            System.out.println("Verificando o email da coordenação...");
            List<Email> emailConfirmacao = emailController.emails(subject, null, email);
            for (Email oneEmail : emailConfirmacao) {
                String body = oneEmail.getBody();

                Pattern autorizadoPattern = Pattern.compile("(?i)\\bAutorizado\\b");
                Matcher autorizadoMatcher = autorizadoPattern.matcher(body);
                if (autorizadoMatcher.find()) {
                    execution.setVariable("anuencia", true);
                } else {
                    execution.setVariable("anuencia", false);

                    Pattern justificativaPattern = Pattern.compile("(?i)Justificativa:\\s*(.*)");
                    Matcher justificativaMatcher = justificativaPattern.matcher(body);
                    if (justificativaMatcher.find()) {
                        String justificativa = justificativaMatcher.group(1).trim();
                        execution.setVariable("justificativaAnuencia", justificativa);
                    }
                }
            }

            if (!emailConfirmacao.isEmpty()) {
                recebeuEmail = true;
                for(Email marcarEmail : emailConfirmacao) {
                    markEmail.markEmailAsRead(marcarEmail.getUid());
                }
            } else {
                recebeuEmail = false;
            }
            execution.setVariable("recebeuEmail", recebeuEmail);
        } else{
            execution.setVariable("verificaEmail", 1);
            System.out.println("Passou ao verificar o email da Coordenacao.");
        }
    }
}