package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnviarEmailSolicitacaoAnuenciaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController senderEmail = new SenderEmailController();
        String email = "davidsoncsantos45@gmail.com";

        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");

        String subject = "Solicitação de anuência – Aluno: " + aluno + " | Título: " + titulo_trabalho;

        String body = "Prezado(a) Coordenador(a),<br><br>"
                + "Solicitamos, por meio deste, a anuência institucional referente à defesa de mestrado do(a) discente " + aluno
                + ", cujo trabalho intitula-se: &quot;" + titulo_trabalho + "&quot;.<br><br>"
                + "Gentilmente, solicitamos que a resposta a esta solicitação contenha, de forma objetiva:<br>"
                + "- A autorização para realização da defesa, ou<br>"
                + "- A negativa, acompanhada da respectiva justificativa.<br><br>"
                + "O retorno desta informação é imprescindível para a continuidade dos trâmites acadêmicos referentes ao processo de defesa.<br><br>"
                + "Permanecemos à disposição para quaisquer esclarecimentos.<br><br>"
                + "Atenciosamente,<br><br>"
                + "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>"
                + "IFES – Campus Serra";

        senderEmail.sendEmail(email, subject, body);
    }
}
