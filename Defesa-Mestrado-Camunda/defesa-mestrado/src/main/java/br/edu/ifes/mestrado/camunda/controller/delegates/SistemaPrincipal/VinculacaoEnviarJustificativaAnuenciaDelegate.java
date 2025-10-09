package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinculacaoEnviarJustificativaAnuenciaDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(VinculacaoEnviarJustificativaAnuenciaDelegate.class);

    @Override
    public void execute(DelegateExecution execution){
        LOGGER.info("Chegou em 'Jusitifcativa de vinculacao negada reportada' no BPMN");

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("anuenciaNegada")
                .count();

        LOGGER.info("Bussiness Key: " + businessKey + " Envio de justificativa da anuência");
        if (count > 0){
            String justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");

            runtimeService.createMessageCorrelation("anuenciaNegada")
                    .setVariable("justificativaAnuencia", justificativaAnuencia)
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
        } else {
            LOGGER.warn("Nenhuma instância encontrada esperando pela mensagem Envio de justificativa no sistema principal.");
        }

    }
}
