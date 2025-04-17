package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnvioDefesaNegada implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");

        String subject = "Defesa de Mestrado Negada - " + aluno;

        String body = "Prezado(a) Orientador(a),\n\n"
                + "Gostaríamos de informar que a defesa de mestrado do(a) aluno(a) " + aluno
                + ", com o título do trabalho \"" + titulo_trabalho + "\", foi negada. "
                + "Abaixo estão os detalhes da justificativa para essa decisão:\n\n"
                + "Justificativa para a negativa da defesa:\n\"" + justificativaAnuencia + "\"\n\n"
                + "Agradecemos sua atenção a este assunto e permanecemos à disposição para quaisquer esclarecimentos adicionais.\n\n"
                + "Atenciosamente,\n\n"
                + "PPComp \n"
                + "IFES - Campus Serra";

        emailSender.sendEmail(emailOrientador, subject, body);
    }
}
