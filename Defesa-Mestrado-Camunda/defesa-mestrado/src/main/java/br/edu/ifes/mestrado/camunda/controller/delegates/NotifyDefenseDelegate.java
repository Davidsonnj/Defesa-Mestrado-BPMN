package br.edu.ifes.mestrado.camunda.controller.delegates;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;

import java.util.List;

public class NotifyDefenseDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController emailSender = new SenderEmailController();

        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");

        // Criando o assunto e o corpo do e-mail
        String subject = "Informações sobre a Defesa de Trabalho de " + aluno;
        String body = "Prezado(a) " + aluno + ",\n\n"
                + "Gostaríamos de informar que a sua defesa de trabalho, intitulada '"
                + tituloTrabalho + "', está agendada conforme os detalhes a seguir:\n\n"
                + "Título do Trabalho: " + tituloTrabalho + "\n"
                + "Aluno: " + aluno + "\n\n"
                + "A defesa ocorrerá de acordo com o cronograma e locais previamente definidos.\n\n"
                + "Caso haja alguma dúvida ou se necessitar de mais informações, "
                + "por favor, entre em contato conosco.\n\n"
                + "Atenciosamente,\n"
                + "Equipe de Defesa de Trabalhos";

        emailSender.sendEmail(emailAluno, subject, body);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        List<Execution> execucoes = runtimeService.createExecutionQuery()
                .processDefinitionKey("Process_1eeteym")
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

        System.out.println(execucoes);

        if (execucoes.isEmpty()) {
            if (businessKey != null) {

                runtimeService.createMessageCorrelation("confirmarDefesaMessage")
                        .setVariable("aluno", aluno)
                        .setVariable("titulo_trabalho", tituloTrabalho)
                        .setVariable("emailAluno", emailAluno)
                        .processInstanceBusinessKey(businessKey)
                        .correlate();
            } else {
                System.out.println("⚠️ NotifyDefense - Business Key está NULL! Tentando correlacionar sem Business Key...");
                runtimeService.createMessageCorrelation("confirmarDefesaMessage")
                        .setVariable("aluno", aluno)
                        .setVariable("titulo_trabalho", tituloTrabalho)
                        .setVariable("emailAluno", emailAluno)
                        .correlate();
            }
        } else {

            System.out.println("Processo de confirmação da defesa já existe para o aluno: " + aluno);
        }
    }
}
