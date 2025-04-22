package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnviarJustificativaAnuenciaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        System.out.println("Chegou em 'Jusitifcativa de defesa negada reportada' no BPMN");

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaNegada")
                .count();

        System.out.println("Bussiness Key: " + businessKey + " Envio de justificativa da anuência");
        if (count > 0){
            String justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");

            runtimeService.createMessageCorrelation("DefesaNegada")
                    .setVariable("justificativaAnuencia", justificativaAnuencia)
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem Envio de justificativa no sistema principal.");
        }

    }
}
