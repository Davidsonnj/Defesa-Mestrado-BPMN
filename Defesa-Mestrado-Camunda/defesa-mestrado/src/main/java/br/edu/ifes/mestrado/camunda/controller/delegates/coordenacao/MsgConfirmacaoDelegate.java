package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class MsgConfirmacaoDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("MsgCoordenacao")
                .count();
        System.out.println("Bussiness Key: " + businessKey + " Envio de resposta da anuência");
        if (count > 0){
            Boolean anuencia = (Boolean) execution.getVariable("anuencia");
            String justificativaAnuencia;

            if (execution.hasVariable("justificativaAnuencia")){
                justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");
                execution.setVariable("justificativaAnuencia", anuencia);

            }else {
                justificativaAnuencia = "Não foi informada nenhuma justificativa.";
            }

            runtimeService.createMessageCorrelation("MsgCoordenacao")
                    .setVariable("justificativaAnuencia", justificativaAnuencia)
                    .setVariable("anuencia", anuencia)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem Envio de dados do aluno.");
        }

    }

}
