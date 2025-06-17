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
        String body = "Prezado(a) Orientador(a),\n\n" +
                "Comunicamos que, conforme anuência da Coordenação do Programa de Pós-Graduação em Computação Aplicada (PPComp), está autorizada a realização da defesa de dissertação do(a) mestrando(a) " + aluno + ", intitulada:\n\n" +
                "\"" + titulo_trabalho + "\"\n\n" +
                "A defesa está agendada para:\n" +
                "Data: " + dataDefesa + "\n" +
                "Hora: " + horaDefesa + "\n" +
                "Local: " + localDefesa + "\n\n" +
                "Permanecemos à disposição para quaisquer esclarecimentos adicionais.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES – Campus Serra";


        emailSender.sendEmail(emailOrientador, subject, body);

    }
}
