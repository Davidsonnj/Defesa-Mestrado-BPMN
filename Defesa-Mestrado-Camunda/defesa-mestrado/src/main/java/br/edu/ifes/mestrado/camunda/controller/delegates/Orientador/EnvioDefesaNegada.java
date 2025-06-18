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

        String subject = "Comunicação de Indeferimento da Defesa de Mestrado – " + aluno;

        String body = "Prezado(a) Orientador(a),\n\n" +
                "Informamos que a defesa de mestrado do(a) discente " + aluno + ", intitulada \"" + titulo_trabalho + "\", foi indeferida. " +
                "Segue abaixo a justificativa detalhada para essa decisão:\n\n" +
                "Justificativa:\n\"" + justificativaAnuencia + "\"\n\n" +
                "Agradecemos a atenção dispensada e colocamo-nos à disposição para quaisquer esclarecimentos adicionais.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES – Campus Serra";


        emailSender.sendEmail(emailOrientador, subject, body);
    }
}
