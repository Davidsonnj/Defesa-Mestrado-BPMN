package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal.DefesaCanceladaDelegate;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgConfirmacaoDefesaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgConfirmacaoDefesaDelegate.class);

    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        if (businessKey == null) {
            LOGGER.error("BusinessKey está nulo! Não é possível correlacionar a mensagem.");
            return;
        }

        LOGGER.info("Tentando correlacionar mensagem 'DefesaCancelada' para BusinessKey: {}", businessKey);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        boolean isWaiting = false;
        for (int i = 0; i < 5; i++) {
            long count = runtimeService.createExecutionQuery()
                    .processInstanceBusinessKey(businessKey)
                    .messageEventSubscriptionName("DefesaConfirmada")
                    .count();

            if (count > 0) {
                isWaiting = true;
                break;
            }

            try {
                Thread.sleep(1000);  // Espera 1 segundo antes de tentar de novo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isWaiting) {
            long idConfirmacaoDefesa = (long) execution.getVariable("idConfirmacaoDefesa");

            runtimeService.createMessageCorrelation("DefesaConfirmada")
                    .processInstanceBusinessKey(businessKey)
                    .setVariable("idConfirmacaoDefesa", idConfirmacaoDefesa)
                    .correlate();
            LOGGER.info("Mensagem 'DefesaConfirmada' correlacionada com sucesso.");
        } else {
            LOGGER.warn("NENHUMA instância encontrada para a mensagem 'DefesaConfirmada'. A correlação não foi tentada.");
        }

    }

}
