package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.emailAPI.controller.EmailController;
import br.edu.ifes.mestrado.emailAPI.model.Email;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;

public class BuscarEmailDefesaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        EmailController emailController = new EmailController();
        Boolean recebeuEmail = false;

        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");
        String aluno = (String) execution.getVariable("aluno");

        String subject = String.format("Confirmação da Defesa - %s",titulo_trabalho);
        System.out.println(subject);

        System.out.println("Verificando o email do aluno");
        List<Email> emailConfirmacao = emailController.emails(subject, null, emailAluno);

        if(!emailConfirmacao.isEmpty()) {
            recebeuEmail = true;
        } else {
            recebeuEmail = false;
        }
        execution.setVariable("recebeuEmail", recebeuEmail);
    }
}