package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import io.github.cdimascio.dotenv.Dotenv;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class VinculacaoEnviarEmailSolicitacaoAnuenciaDelegate implements JavaDelegate {
    private static final Dotenv dotenv = Dotenv.load();
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController senderEmail = new SenderEmailController();

        String aluno = (String) execution.getVariable("alunoNome");
        String tema = (String) execution.getVariable("tema");
        String matricula = (String) execution.getVariable("matricula");
        String email = dotenv.get("EMAIL_COORDENACAO");

        // Verificação básica (opcional)
        if (aluno == null || tema == null || email == null) {
            throw new IllegalArgumentException("Dados obrigatórios ausentes: aluno, tema ou e-mail.");
        }

        String subject = "Solicitação de anuência da vinculação – Aluno: " + aluno + " | Título: " + tema;

        String body = "Prezado(a) Coordenador(a),<br><br>"
                + "Solicitamos, por meio deste, a anuência institucional referente à vinculação do(a) discente <strong>" + aluno + " (" + matricula + ")" + "</strong>,"
                + " cujo o tema intitula-se: <strong>&quot;" + tema + "&quot;</strong>.<br><br>"
                + "Gentilmente, solicitamos que a resposta a esta solicitação contenha, de forma objetiva:<br>"
                + "- A autorização para a vinculação, ou<br>"
                + "- A negativa, acompanhada da respectiva justificativa.<br><br>"
                + "O retorno desta informação é imprescindível para a continuidade dos trâmites acadêmicos referentes ao processo.<br><br>"
                + "Permanecemos à disposição para quaisquer esclarecimentos.<br><br>"
                + "Atenciosamente,<br><br>"
                + "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>"
                + "IFES – Campus Serra";

        senderEmail.sendEmail(email, subject, body);
    }
}
