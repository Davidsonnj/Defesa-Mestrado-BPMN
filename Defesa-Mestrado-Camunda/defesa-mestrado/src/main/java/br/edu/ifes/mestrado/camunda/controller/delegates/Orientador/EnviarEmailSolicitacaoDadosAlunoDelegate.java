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

        String subject = "Solicitação de Dados do Aluno" + aluno;
        String body = "Prezado(a) Orientador(a),\n\n"
                + "O PPComp do IFES - Campus Serra e esta entrando em contato para solicitar os seguintes dados do aluno " + aluno + " (siga exatamente essa formatação, sem nenhuma adição de informações):\n\n"
                + "- Data:\n"
                + "- Hora:\n"
                + "- Local:\n\n"
                + "- Banca:\n"
                + "- Nome:\n"
                + "- Email:\n"
                + "- Instituição:\n"
                + "- Minicurrículo\n\n"
                + "Por favor, preencha essas informações o mais breve possível para darmos continuidade ao processo.\n\n"
                + "Atenciosamente,\n"
                + "PPComp - IFES - Campus Serra";

        senderEmail.sendEmail(emailOrientador, subject, body);
    }

}
