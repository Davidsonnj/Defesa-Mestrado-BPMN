package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SolicitacaoUploadDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution)throws Exception {
        SenderEmailController emailSender = new SenderEmailController();
        String emailAluno = (String) execution.getVariable("emailAluno");
        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");

        String subject = "Solicitação de upload da tese - " + titulo_trabalho;

        String body = "Prezado(a) Aluno(a),\n\n"
                + "Gostaríamos de informar que, para dar continuidade ao processo de defesa de mestrado, "
                + "solicitamos que envie a versão final da sua tese, com o título \"" + titulo_trabalho + "\", "
                + "para este e-mail.\n\n"
                + "Por favor, envie o arquivo da tese em formato PDF até a data limite, para que possamos agendar a sua defesa. "
                + "Caso já tenha enviado o documento, por favor desconsidere este lembrete.\n\n"
                + "Agradecemos sua atenção e ficamos à disposição para quaisquer esclarecimentos adicionais.\n\n"
                + "Atenciosamente,\n\n"
                + "PPComp \n"
                + "IFES - Campus Serra";

        emailSender.sendEmail(emailAluno, subject, body);
    }
}
