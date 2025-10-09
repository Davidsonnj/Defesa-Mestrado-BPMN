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
        String tipoDefesa = (String) execution.getVariable("tipoDefesa");

        String subject;
        String body;

        if (tipoDefesa.equals("qualificacao")) {

            subject = "Exame de Qualificação Negada - " + aluno;

            body = "Prezado(a) Aluno(a),<br><br>"
                    + "Informamos que a solicitação do exame de qualificação, referente ao trabalho intitulado &quot;" + titulo_trabalho + "&quot;, foi indeferida.<br><br>"
                    + "Motivo da negativa:<br>"
                    + "&quot;" + justificativaAnuencia + "&quot;<br><br>"
                    + "Caso haja dúvidas ou necessidade de esclarecimentos adicionais, estamos à disposição.<br><br>"
                    + "Atenciosamente,<br><br>"
                    + "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>"
                    + "IFES - Campus Serra";

        }else{
            subject = "Defesa de Dissertação - " + aluno;

            body = "Prezado(a) Aluno(a),<br><br>"
                    + "Informamos que a solicitação de defesa de dissertação, referente ao trabalho intitulado &quot;" + titulo_trabalho + "&quot;, foi indeferida.<br><br>"
                    + "Motivo da negativa:<br>"
                    + "&quot;" + justificativaAnuencia + "&quot;<br><br>"
                    + "Caso haja dúvidas ou necessidade de esclarecimentos adicionais, estamos à disposição.<br><br>"
                    + "Atenciosamente,<br><br>"
                    + "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>"
                    + "IFES - Campus Serra";
        }

        emailSender.sendEmail(emailOrientador, subject, body);
    }
}
