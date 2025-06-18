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
                "Informamos que a sua defesa do trabalho intitulado \"" + tituloTrabalho + "\" foi cancelada, em razão da ausência de resposta aos e-mails enviados anteriormente.\n\n" +
                "Compreendemos que imprevistos podem ocorrer e, caso ainda tenha interesse em reagendar a sua defesa, solicitamos que entre em contato conosco o quanto antes. " +
                "Nossa equipe está à disposição para prestar os devidos esclarecimentos e orientações necessárias para a retomada do processo.\n\n" +
                "Aguardamos seu retorno.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES - Campus Serra";


        emailSender.sendEmail(emailAluno, subject, body);
    }
}
