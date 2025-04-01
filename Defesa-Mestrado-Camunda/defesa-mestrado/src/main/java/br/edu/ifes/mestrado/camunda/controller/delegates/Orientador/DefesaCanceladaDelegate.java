package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DefesaCanceladaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String businessKey = execution.getProcessBusinessKey();

        String subject = "Cancelamento da Defesa de Trabalho de " + aluno;
        String body = "Prezado(a) Coordenador(a),\n\n" +
                "Gostaríamos de informá-lo(a) que, infelizmente, a defesa do trabalho, intitulada \"" + tituloTrabalho + "\", foi cancelada. " +
                "Isso ocorreu devido à falta de resposta aos e-mails enviados ao aluno.\n\n" +
                "Atenciosamente,\n" +
                "PPComp\n" +
                "IFES - Campus Serra\n";

        emailSender.sendEmail(emailOrientador, subject, body);
    }
}

