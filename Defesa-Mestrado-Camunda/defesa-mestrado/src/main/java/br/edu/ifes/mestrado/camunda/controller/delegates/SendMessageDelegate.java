package br.edu.ifes.mestrado.camunda.controller.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.RuntimeService;

import java.util.HashMap;
import java.util.Map;

public class SendMessageDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");

        if (emailAluno == null || emailAluno.isEmpty()) {
            System.out.println("⚠️ E-mail do aluno não foi fornecido. Impossível enviar o e-mail.");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("aluno", aluno);
        variables.put("titulo_trabalho", tituloTrabalho);
        variables.put("emailAluno", emailAluno);

        if (businessKey != null) {
            runtimeService.createMessageCorrelation("dadosAlunos")
                    .setVariables(variables)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            System.out.println("⚠️ SendMessageDelegate - Business Key está NULL! Tentando correlacionar sem Business Key...");
            runtimeService.createMessageCorrelation("dadosAlunos")
                    .setVariables(variables)
                    .correlateAll();
        }
    }
}
