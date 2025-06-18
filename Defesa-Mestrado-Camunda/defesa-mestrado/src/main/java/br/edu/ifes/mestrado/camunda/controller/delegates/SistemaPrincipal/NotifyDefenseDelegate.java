package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyDefenseDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController emailSender = new SenderEmailController();

        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");
        String emailOrientador = (String) execution.getVariable("emailOrientador");

        // Criando o assunto e o corpo do e-mail
        String subject = "Informações sobre a Defesa de Trabalho de " + aluno;
        String body = "Prezado(a) " + aluno + ",\n\n" +
                "Informamos que a sua defesa de trabalho, intitulada \"" + tituloTrabalho + "\", está agendada conforme os detalhes abaixo:\n\n" +
                "Título do Trabalho: " + tituloTrabalho + "\n" +
                "Aluno(a): " + aluno + "\n\n" +
                "Solicitamos, por gentileza, que confirme sua participação respondendo a este e-mail.\n\n" +
                "Ressaltamos que este pedido de confirmação será enviado por até três dias consecutivos. Caso não confirme, por favor, desconsidere as mensagens subsequentes.\n\n" +
                "A defesa será realizada conforme o cronograma e local previamente definidos.\n\n" +
                "Caso tenha alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "IFES – Campus Serra";


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

                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("aluno", aluno);
                variables.put("titulo_trabalho", tituloTrabalho);
                variables.put("emailAluno", emailAluno);
                variables.put("emailOrientador", emailOrientador);


                runtimeService.createMessageCorrelation("confirmarDefesaMessage")
                        .setVariables(variables)
                        .processInstanceBusinessKey(businessKey)
                        .correlate();
            } else {
                System.out.println("⚠️ NotifyDefense - Business Key está NULL!");
            }
        } else {

            System.out.println("Processo de confirmação da defesa já existe para o aluno: " + aluno);
        }
    }
}
