package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnviarEmailSolicitacaoDadosAlunoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController senderEmail = new SenderEmailController();

        String aluno = (String) execution.getVariable("aluno");
        String emailOrientador = (String) execution.getVariable("emailOrientador");

        String subject = "Solicitação de dados para agendamento de defesa – Aluno(a) " + aluno;
        String body = "Prezado(a) Orientador(a),\n\n" +
                "Solicitamos, por gentileza, o envio das informações referentes à defesa de dissertação do(a) discente " + aluno + ", necessárias para a formalização do processo no âmbito do PPComp – IFES Campus Serra.\n\n" +
                "As informações requeridas são:\n\n" +
                "- Data da defesa;\n" +
                "- Horário;\n" +
                "- Local;\n\n" +
                "Composição da banca examinadora:\n" +
                "- Nome de cada membro;\n" +
                "- E-mail institucional;\n" +
                "- Instituição de origem;\n" +
                "- Minicurrículo (resumido).\n\n" +
                "Agradecemos o envio dessas informações com a maior brevidade possível para que possamos dar prosseguimento aos trâmites acadêmicos.\n\n" +
                "Permanecemos à disposição para eventuais esclarecimentos.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES – Campus Serra";


        senderEmail.sendEmail(emailOrientador, subject, body);
    }

}
