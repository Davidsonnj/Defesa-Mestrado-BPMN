package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DefesaCanceladaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();
        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");

        String subject = "Cancelamento da Defesa de Trabalho de " + aluno;
        String body = "Prezado(a) " + aluno + ",\n\n" +
                "Espero que esteja bem.\n\n" +
                "Gostaríamos de informá-lo(a) que, infelizmente, a sua defesa do trabalho, intitulada \"" + tituloTrabalho + "\", foi cancelada. " +
                "Isso ocorreu devido à falta de resposta aos e-mails enviados anteriormente.\n\n" +
                "Entendemos que imprevistos podem acontecer, e caso você ainda tenha interesse em reagendar a sua defesa, " +
                "pedimos que entre em contato conosco o quanto antes. Nossa equipe estará disponível para fornecer todas as informações necessárias " +
                "para que você possa tomar as providências necessárias.\n\n" +
                "Aguardamos o seu retorno.\n\n" +
                "Atenciosamente,\n" +
                "PPComp\n" +
                "IFES - Campus Serra\n";

        emailSender.sendEmail(emailAluno, subject, body);
    }
}
