package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviarJustificativaAnuenciaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarJustificativaAnuenciaDelegate.class);

    @Override
    public void execute(DelegateExecution execution){
        LOGGER.info("Chegou em 'Jusitifcativa de defesa negada reportada' no BPMN");

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaNegada")
                .count();

        LOGGER.info("Bussiness Key: " + businessKey + " Envio de justificativa da anuência");
        if (count > 0){
            String justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");

            runtimeService.createMessageCorrelation("DefesaNegada")
                    .setVariable("justificativaAnuencia", justificativaAnuencia)
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
        } else {
            LOGGER.warn("Nenhuma instância encontrada esperando pela mensagem Envio de justificativa no sistema principal.");
        }

    }
}
