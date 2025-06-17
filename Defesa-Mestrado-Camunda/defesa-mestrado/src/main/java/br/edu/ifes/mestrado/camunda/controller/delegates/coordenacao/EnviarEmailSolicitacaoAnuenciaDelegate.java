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
        String body = "Prezado(a) Coordenador(a),\n\n"
                + "Solicitamos, por meio deste, a anuência institucional referente à defesa de mestrado do(a) discente " + aluno +
                ", cujo trabalho intitula-se: \"" + titulo_trabalho + "\".\n\n"
                + "Gentilmente, solicitamos que a resposta a esta solicitação contenha, de forma objetiva:\n"
                + "- A autorização para realização da defesa, ou\n"
                + "- A negativa, acompanhada da respectiva justificativa.\n\n"
                + "O retorno desta informação é imprescindível para a continuidade dos trâmites acadêmicos referentes ao processo de defesa.\n\n"
                + "Permanecemos à disposição para quaisquer esclarecimentos.\n\n"
                + "Atenciosamente,\n\n"
                + "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n"
                + "IFES – Campus Serra";
        senderEmail.sendEmail(email, subject, body);
    }
}
