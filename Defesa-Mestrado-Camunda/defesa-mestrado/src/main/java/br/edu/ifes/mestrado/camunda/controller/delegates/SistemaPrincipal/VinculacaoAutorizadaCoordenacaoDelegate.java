package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class VinculacaoAutorizadaCoordenacaoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        String businessKey = execution.getProcessBusinessKey();


        RuntimeService runtimeService =  execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("anuenciaAprovada")
                .count();
        if (count > 0) {
            runtimeService.createMessageCorrelation("anuenciaAprovada")
                    .processInstanceBusinessKey(businessKey)
                    .correlate();

        }
    }
}
