package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;

import java.util.List;

public class SolicitarUploadDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("SolicitacaoUpload")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("SolicitacaoUpload")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            System.out.println("Mensagem de Solicitação de Upload Enviado com sucesso!");
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem de Solicitação de Upload.");
        }
    }
}
