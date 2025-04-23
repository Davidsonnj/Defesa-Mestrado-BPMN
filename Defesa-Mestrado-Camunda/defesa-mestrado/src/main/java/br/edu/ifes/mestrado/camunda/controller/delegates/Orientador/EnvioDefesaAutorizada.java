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
        String horaDefesa = (String) execution.getVariable("dataHora");
        String localDefesa = (String) execution.getVariable("localDefesa");

        String subject = "Autorização para Defesa de Mestrado – " + aluno;
        String body = "Prezado(a) Orientador(a),\n\n"
                + "Informamos que, conforme anuência da Coordenação do Programa de Pós-Graduação, está autorizada a realização da defesa de dissertação do(a) mestrando(a) " + aluno + ", intitulada:\n\n"
                + "\"" + titulo_trabalho + "\"\n\n"
                + "A defesa está agendado para: "
                + "Data de defesa: " + dataDefesa + "\n"
                + "Hora de defesa: " + horaDefesa + "\n"
                + "Local: " + localDefesa + "\n\n"
                + "Atenciosamente,\n\n"
                + "PPComp \n"
                + "IFES - Campus Serra";

        emailSender.sendEmail(emailOrientador, subject, body);

    }
}
