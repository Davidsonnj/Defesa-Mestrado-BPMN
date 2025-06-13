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

        String subject = "Solicitação de Anuência do aluno " + aluno + "Titulo da dissertação: " + titulo_trabalho;
        String body = "Prezado(a) Coordenador(a),\n\n"
                + "Estamos entrando em contato para solicitar a anuência do " + aluno + " que tem a defesa intitulada de '"+ titulo_trabalho +"', favor seguir exatamente essa formatação, sem nenhuma adição de informações:\n\n"
                + "O assunto deve ser: Anuencia de defesa - 'Titutlo da defesa' "
                + "Caso tenha sido negado enviar a mensagem com a seguinta formatação: 'Jutisficativa: (Colocar o motivo da rejeição)'"
                + "Caso seja autorizado enviar no corpo da mensagem somente a palavra Autorizado\n\n"
                + "Por favor, preencha essas informações o mais breve possível para darmos continuidade ao processo.\n\n"
                + "Atenciosamente,\n"
                + "PPComp \n"
                + "IFES - Campus Serra";
        senderEmail.sendEmail(email, subject, body);
    }
}
