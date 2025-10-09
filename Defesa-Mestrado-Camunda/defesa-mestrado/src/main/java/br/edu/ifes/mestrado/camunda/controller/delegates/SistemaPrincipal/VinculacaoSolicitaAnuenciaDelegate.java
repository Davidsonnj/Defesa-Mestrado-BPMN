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

public class VinculacaoSolicitaAnuenciaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(VinculacaoSolicitaAnuenciaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        DefesaDAO defesaDAO = new DefesaDAO();

        List<Execution> execucoes = runtimeService.createExecutionQuery()
                .processDefinitionKey("Process_0z4nyrp")
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

        System.out.println(execucoes);

        if (execucoes.isEmpty()) {
            if (businessKey != null) {
                String alunoNome = (String) execution.getVariable("alunoNome");
                String emailAluno = (String) execution.getVariable("emailAluno");
                String emailOrientador = (String) execution.getVariable("emailOrientador");
                String nomeOrientador = (String) execution.getVariable("nomeOrientador");
                String matricula = (String) execution.getVariable("matricula");
                String periodoInicio =  (String) execution.getVariable("periodoInicio");
                String tema =  (String) execution.getVariable("tema");
                Long idEmail = (Long) execution.getVariable("idEmail");

                Map<String, Object> variables = new HashMap<>();
                variables.put("alunoNome", alunoNome);
                variables.put("emailAluno", emailAluno);
                variables.put("emailOrientador", emailOrientador);
                variables.put("nomeOrientador", nomeOrientador);
                variables.put("matricula", matricula);
                variables.put("periodoInicio", periodoInicio);
                variables.put("tema", tema);
                variables.put("idEmail", idEmail);

                runtimeService.createMessageCorrelation("VinculacaoSolicitacaoAnuencia")
                        .processInstanceBusinessKey(businessKey)
                        .setVariables(variables)
                        .correlate();

                LOGGER.info("Mensagem de anuência da vinculação solicitado com sucesso!");
            } else {
                LOGGER.error("Nenhuma instância encontrada esperando pela mensagem de solicitacao de anuência da vinculação.");
            }
        }
    }
}
