package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefesaoConfirmadaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefesaoConfirmadaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaConfirmadaOrientador")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("DefesaConfirmadaOrientador")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            LOGGER.info("Mensagem de cadastro Solicitado com sucesso!");
        } else {
            LOGGER.warn("Nenhuma instância encontrada esperando pela mensagem de Cadastro Solicitado.");
        }
    }
}
