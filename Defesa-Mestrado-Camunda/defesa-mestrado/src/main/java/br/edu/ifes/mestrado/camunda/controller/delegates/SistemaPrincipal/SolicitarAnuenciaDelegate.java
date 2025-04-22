package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolicitarAnuenciaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        List<Execution> execucoes = runtimeService.createExecutionQuery()
                .processDefinitionKey("Process_0z4nyrp")
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

        System.out.println(execucoes);

        if (execucoes.isEmpty()) {
            if (businessKey != null) {
                String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
                String aluno = (String) execution.getVariable("aluno");

                runtimeService.createMessageCorrelation("SolicitacaoAnuencia")
                        .processInstanceBusinessKey(businessKey)
                        .setVariable("titulo_trabalho", titulo_trabalho)
                        .setVariable("aluno", aluno)
                        .correlate();

                System.out.println("Mensagem de anuência solicitado com sucesso!");
            } else {
                System.out.println("Nenhuma instância encontrada esperando pela mensagem de solicitacao de anuência.");
            }
        }
    }
}
