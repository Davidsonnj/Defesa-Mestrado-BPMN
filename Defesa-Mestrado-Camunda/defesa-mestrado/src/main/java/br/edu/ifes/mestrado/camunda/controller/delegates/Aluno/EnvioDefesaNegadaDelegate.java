package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnvioDefesaNegadaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");

        String subject = "Defesa de Mestrado Negada - " + aluno;

        String body = "Prezado(a) Aluno(a),\n\n"
                + "Informamos que a solicitação de defesa de mestrado, referente ao trabalho intitulado \"" + titulo_trabalho + "\", foi indeferida.\n\n"
                + "Motivo da negativa:\n"
                + "\"" + justificativaAnuencia + "\"\n\n"
                + "Caso haja dúvidas ou necessidade de esclarecimentos adicionais, estamos à disposição.\n\n"
                + "Atenciosamente,\n\n"
                + "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n"
                + "IFES - Campus Serra";


        emailSender.sendEmail(emailOrientador, subject, body);
    }
}
