package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefesaoConfirmadaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefesaoConfirmadaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        int idDefesa = (Integer) execution.getVariable("idDefesaBD");
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        DefesaDAO defesaDAO = new DefesaDAO();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("DefesaConfirmadaOrientador")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("DefesaConfirmadaOrientador")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();

            defesaDAO.atualizarStatus(idDefesa, "DefesaConfirmada");

            LOGGER.info("Mensagem de cadastro Solicitado com sucesso!");
        } else {
            LOGGER.warn("Nenhuma instância encontrada esperando pela mensagem de Cadastro Solicitado.");
        }
    }
}
