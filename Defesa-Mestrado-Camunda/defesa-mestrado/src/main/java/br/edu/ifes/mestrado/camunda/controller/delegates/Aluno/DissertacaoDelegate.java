package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DissertacaoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        String businessKey = execution.getProcessBusinessKey();

        RuntimeService runtimeService =  execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("RegistrarDissertacao")
                .count();
        if (count > 0) {
            String caminhosDosAnexos = (String) execution.getVariable("caminhosDosAnexos");

            runtimeService.createMessageCorrelation("RegistrarDissertacao")
                    .processInstanceBusinessKey(businessKey)
                    .setVariable("caminhosDosAnexos", caminhosDosAnexos)
                    .correlate();
        }
    }
}
