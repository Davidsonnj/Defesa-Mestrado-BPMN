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

        String subject = "Cancelamento da Defesa de Dissertação do(a) discente " + aluno;
        String body = "Prezado(a) Coordenador(a),\n\n" +
                "Comunicamos que a defesa da dissertação intitulada \"" + tituloTrabalho + "\", de autoria do(a) discente " + aluno + ", foi cancelada. " +
                "O cancelamento se deu em virtude da ausência de resposta aos e-mails encaminhados ao(à) referido(a) discente.\n\n" +
                "Permanecemos à disposição para quaisquer esclarecimentos que se fizerem necessários.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES – Campus Serra";


        emailSender.sendEmail(emailOrientador, subject, body);
    }
}

