package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao.MsgConfirmacaoDelegate;
import br.edu.ifes.mestrado.camunda.model.Banca;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnviarDadosVinculacao implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarDadosVinculacao.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        String businessKey = execution.getProcessBusinessKey();

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


        if (businessKey != null) {
            runtimeService.createMessageCorrelation("dadosAlunosVinculacaoSistema")
                    .setVariables(variables)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            LOGGER.info("SendMessageDelegate 'dadosAlunos' - Business Key está NULL! Tentando correlacionar sem Business Key...");
            runtimeService.createMessageCorrelation("dadosAlunosVinculacaoSistema")
                    .setVariables(variables)
                    .correlateAll();
        }
    }
}

