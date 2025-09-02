package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
import org.camunda.bpm.engine.runtime.Execution;

public class DefesaCanceladaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefesaCanceladaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        if (businessKey == null) {
            LOGGER.error("BusinessKey está nulo! Não é possível correlacionar a mensagem.");
            return;
        }

        LOGGER.info("Tentando correlacionar mensagem 'DefesaCancelada' para BusinessKey: {}", businessKey);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        List<Execution> waitingExecutions = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaCancelada")
                .list();

        long count = waitingExecutions.size();
        LOGGER.info("Query encontrou {} execuções esperando pela mensagem 'DefesaCancelada'.", count);

        if (count > 0) {
            LOGGER.info("Encontradas instâncias. Tentando correlacionar a mensagem...");
            runtimeService.createMessageCorrelation("DefesaCancelada")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            LOGGER.info("Mensagem 'DefesaCancelada' correlacionada com sucesso!");
        } else {
            LOGGER.warn("NENHUMA instância encontrada para a mensagem 'DefesaCancelada'. A correlação não foi tentada.");
        }
    }
}