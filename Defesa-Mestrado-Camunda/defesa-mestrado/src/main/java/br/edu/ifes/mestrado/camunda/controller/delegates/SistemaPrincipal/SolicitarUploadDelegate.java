package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SolicitarUploadDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitarUploadDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        int idDefesa = (Integer) execution.getVariable("idDefesaBD");

        DefesaDAO defesaDAO = new DefesaDAO();

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("SolicitacaoUpload")
                .count();

        if (count > 0) {
            runtimeService.createMessageCorrelation("SolicitacaoUpload")
                    .processInstanceBusinessKey(businessKey)
                    .correlateAll();
            defesaDAO.atualizarStatus(idDefesa, "SolicitacaoUpload");
            LOGGER.info("Mensagem de Solicitação de Upload Enviado com sucesso!");
        } else {
            LOGGER.error("Nenhuma instância encontrada esperando pela mensagem de Solicitação de Upload.");
        }
    }
}
