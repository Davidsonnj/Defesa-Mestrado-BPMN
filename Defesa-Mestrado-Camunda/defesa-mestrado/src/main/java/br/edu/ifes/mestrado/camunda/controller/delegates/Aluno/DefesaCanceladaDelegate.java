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
        String tipoDefesa = (String) execution.getVariable("tipoDefesa");

        String subject;
        String body;

        if (tipoDefesa.equals("qualificacao")) {

            subject = "Cancelamento da Defesa de Trabalho de " + aluno;
            body = "Prezado(a) " + aluno + ",<br><br>" +
                    "Informamos que a sua defesa do trabalho intitulado &quot;" + tituloTrabalho + "&quot; foi cancelada, em razão da ausência de resposta aos e-mails enviados anteriormente.<br><br>" +
                    "Compreendemos que imprevistos podem ocorrer e, caso ainda tenha interesse em reagendar a sua defesa, solicitamos que entre em com seu orientador o quanto antes. " +
                    "Estamos à disposição para prestar os devidos esclarecimentos e orientações necessárias para a retomada do processo.<br><br>" +
                    "Aguardamos seu retorno.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES - Campus Serra";
        }else{
            subject = "Cancelamento do Exame de Qualificação de Trabalho de " + aluno;
            body = "Prezado(a) " + aluno + ",<br><br>" +
                    "Informamos que a seu exame de qualificação do trabalho intitulado &quot;" + tituloTrabalho + "&quot; foi cancelada, em razão da ausência de resposta aos e-mails enviados anteriormente.<br><br>" +
                    "Compreendemos que imprevistos podem ocorrer e, caso ainda tenha interesse em reagendar a sua defesa, solicitamos que entre em com seu orientador o quanto antes. " +
                    "Estamos à disposição para prestar os devidos esclarecimentos e orientações necessárias para a retomada do processo.<br><br>" +
                    "Aguardamos seu retorno.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES - Campus Serra";
        }



        emailSender.sendEmail(emailAluno, subject, body);
    }
}
