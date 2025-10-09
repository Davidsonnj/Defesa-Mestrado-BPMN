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
        String tipoDefesa = (String) execution.getVariable("tipoDefesa");

        String subject;
        String body;

        if (tipoDefesa.equals("qualificacao")){
            subject = "Cancelamento do Exame de Eualificação do(a) Discente " + aluno;
            body = "Prezado(a) Orientador(a),<br><br>" +
                    "Comunicamos que o exame de qualificação intitulada &quot;" + tituloTrabalho + "&quot;, de autoria do(a) discente " + aluno + ", foi cancelada. " +
                    "O cancelamento se deu em virtude da ausência de resposta aos e-mails encaminhados ao(à) referido(a) discente.<br><br>" +
                    "Permanecemos à disposição para quaisquer esclarecimentos que se fizerem necessários.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES – Campus Serra";
        }else {
            subject = "Cancelamento da Defesa de Dissertação do(a) Discente " + aluno;
            body = "Prezado(a) Orientador(a),<br><br>" +
                    "Comunicamos que a defesa da dissertação intitulada &quot;" + tituloTrabalho + "&quot;, de autoria do(a) discente " + aluno + ", foi cancelada. " +
                    "O cancelamento se deu em virtude da ausência de resposta aos e-mails encaminhados ao(à) referido(a) discente.<br><br>" +
                    "Permanecemos à disposição para quaisquer esclarecimentos que se fizerem necessários.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES – Campus Serra";
        }



        emailSender.sendEmail(emailOrientador, subject, body);
    }
}

