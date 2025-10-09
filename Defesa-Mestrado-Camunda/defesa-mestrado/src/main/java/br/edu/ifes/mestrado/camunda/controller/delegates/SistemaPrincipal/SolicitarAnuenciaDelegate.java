package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolicitarAnuenciaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitarAnuenciaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        int idDefesa = (Integer) execution.getVariable("idDefesaBD");

        DefesaDAO defesaDAO = new DefesaDAO();

        List<Execution> execucoes = runtimeService.createExecutionQuery()
                .processDefinitionKey("Process_0z4nyrp")
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

        System.out.println(execucoes);

        if (execucoes.isEmpty()) {
            if (businessKey != null) {
                String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
                String aluno = (String) execution.getVariable("aluno");
                String tipoDefesa = (String) execution.getVariable("tipoDefesa");

                runtimeService.createMessageCorrelation("SolicitacaoAnuencia")
                        .processInstanceBusinessKey(businessKey)
                        .setVariable("titulo_trabalho", titulo_trabalho)
                        .setVariable("aluno", aluno)
                        .setVariable("tipoDefesa", tipoDefesa)
                        .correlate();
                defesaDAO.atualizarStatus(idDefesa, "SolicitaAnuencia");

                LOGGER.info("Mensagem de anuência solicitado com sucesso!");
            } else {
                LOGGER.error("Nenhuma instância encontrada esperando pela mensagem de solicitacao de anuência.");
            }
        }
    }
}
