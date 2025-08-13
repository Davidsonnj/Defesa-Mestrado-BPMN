package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class DissertacaoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        String businessKey = execution.getProcessBusinessKey();
        long idDocumentoDissertacao = (long) execution.getVariable("idDocumentoDissertacao");
        long idConfirmacaoDefesa = (long) execution.getVariable("idConfirmacaoDefesa");

        RuntimeService runtimeService =  execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("RegistrarDissertacao")
                .count();
        if (count > 0) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("idDocumentoDissertacao", idDocumentoDissertacao);
            variables.put("idConfirmacaoDefesa", idConfirmacaoDefesa);

            String caminhosDosAnexos = (String) execution.getVariable("caminhosDosAnexos");

            runtimeService.createMessageCorrelation("RegistrarDissertacao")
                    .setVariables(variables)
                    .processInstanceBusinessKey(businessKey)
                    .setVariable("caminhosDosAnexos", caminhosDosAnexos)
                    .correlate();
        }
    }
}
