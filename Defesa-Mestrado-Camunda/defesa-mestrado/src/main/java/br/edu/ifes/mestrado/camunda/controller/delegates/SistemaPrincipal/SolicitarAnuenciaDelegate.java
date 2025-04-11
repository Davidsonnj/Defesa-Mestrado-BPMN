package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SolicitarAnuenciaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("SolicitacaoAnuencia")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("SolicitacaoAnuencia")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            System.out.println("Mensagem de anuência solicitado com sucesso!");
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem de solicitacao de anuência.");
        }
    }

}
