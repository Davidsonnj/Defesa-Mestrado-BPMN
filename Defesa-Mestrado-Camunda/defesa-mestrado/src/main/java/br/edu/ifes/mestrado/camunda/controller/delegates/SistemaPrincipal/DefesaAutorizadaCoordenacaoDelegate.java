package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DefesaAutorizadaCoordenacaoDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution){
        String businessKey = execution.getProcessBusinessKey();

        RuntimeService runtimeService =  execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaAutorizada")
                .count();
        if (count > 0) {
            runtimeService.createMessageCorrelation("DefesaAutorizada")
                    .processInstanceBusinessKey(businessKey)
                    .correlate();

        }
    }
}
