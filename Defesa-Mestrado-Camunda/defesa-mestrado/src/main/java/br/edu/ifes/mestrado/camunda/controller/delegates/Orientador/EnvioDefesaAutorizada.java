package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnvioDefesaAutorizada implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");

        String subject = "Autorização para Defesa de Mestrado – " + aluno;
        String body = "Prezado(a) Orientador(a),<br><br>" +
                "Comunicamos que, conforme anuência da Coordenação do Programa de Pós-Graduação em Computação Aplicada (PPComp), está autorizada a realização da defesa de dissertação do(a) mestrando(a) " + aluno + ", intitulada:<br><br>" +
                "&quot;" + titulo_trabalho + "&quot;<br><br>" +
                "A defesa está agendada para:<br>" +
                "Data: " + dataDefesa + "<br>" +
                "Hora: " + horaDefesa + "<br>" +
                "Local: " + localDefesa + "<br><br>" +
                "Permanecemos à disposição para quaisquer esclarecimentos adicionais.<br><br>" +
                "Atenciosamente,<br><br>" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                "IFES – Campus Serra";



        emailSender.sendEmail(emailOrientador, subject, body);

    }
}
