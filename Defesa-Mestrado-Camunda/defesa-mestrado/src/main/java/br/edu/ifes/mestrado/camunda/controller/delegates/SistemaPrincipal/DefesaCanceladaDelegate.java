package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DefesaCanceladaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        System.out.println("Tentando correlacionar mensagem 'DefesaCancelada' para BusinessKey: " + businessKey);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaCancelada")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("DefesaCancelada")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            System.out.println("Mensagem de Defesa Cancelada enviada com sucesso!");
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem Defesa Cancelada.");
        }
    }

}
