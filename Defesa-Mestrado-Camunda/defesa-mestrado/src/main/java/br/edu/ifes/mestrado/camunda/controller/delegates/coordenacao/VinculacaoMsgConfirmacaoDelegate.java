package br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinculacaoMsgConfirmacaoDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(VinculacaoMsgConfirmacaoDelegate.class);

    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        long idAnuenciaCoordenacao = (long) execution.getVariable("idAnuenciaCoordenacao");

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("RespostaCoordencaoVinculacao")
                .count();
        LOGGER.info("Bussiness Key: " + businessKey + " Envio de resposta da anuência");
        if (count > 0){
            Boolean anuencia = (Boolean) execution.getVariable("anuencia");
            String justificativaAnuencia;

            if (execution.hasVariable("justificativaAnuencia")){
                justificativaAnuencia = (String) execution.getVariable("justificativaAnuencia");
                execution.setVariable("justificativaAnuencia", anuencia);

            }else {
                justificativaAnuencia = "Não foi informada nenhuma justificativa.";
            }

            runtimeService.createMessageCorrelation("RespostaCoordencaoVinculacao")
                    .setVariable("justificativaAnuencia", justificativaAnuencia)
                    .setVariable("anuencia", anuencia)
                    .setVariable("idAnuenciaCoordenacao", idAnuenciaCoordenacao)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            LOGGER.warn("NENHUMA instância encontrada para a mensagem 'RespostaCoordencaoVinculacao'. A correlação não foi tentada.");
        }

    }
}
