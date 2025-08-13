package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import br.edu.ifes.mestrado.camunda.model.Banca;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.RuntimeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnviarDadosParaSistemaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String emailAluno = (String) execution.getVariable("emailAluno");
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String nomeOrientador = (String) execution.getVariable("nomeOrientador");
        String emailCoorientador = (String) execution.getVariable("emailCoorientador");
        String nomeCoorientador = (String) execution.getVariable("nomeCoorientador");
        long idDadosIniciais = (long) execution.getVariable("idDadosIniciais");

        String bancaJsonString = execution.getVariableTyped("bancaDefesa").getValue().toString();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Banca> bancaList = objectMapper.readValue(bancaJsonString, new TypeReference<List<Banca>>() {});

        Object bancaDefesa = bancaList;

        // Exemplo de como usar a lista de objetos:
        if (!bancaList.isEmpty()) {
            System.out.println("Nome do primeiro membro da banca: " + bancaList.get(0).getNome());
        }
        if (emailAluno == null || emailAluno.isEmpty()) {
            System.out.println("⚠️ E-mail do aluno não foi fornecido. Impossível enviar o e-mail.");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("aluno", aluno);
        variables.put("titulo_trabalho", tituloTrabalho);
        variables.put("emailAluno", emailAluno);
        variables.put("emailOrientador", emailOrientador);
        variables.put("dataDefesa", dataDefesa);
        variables.put("horaDefesa", horaDefesa);
        variables.put("localDefesa", localDefesa);
        variables.put("bancaDefesa", bancaDefesa);
        variables.put("nomeOrientador", nomeOrientador);
        variables.put("nomeCoorientador", nomeCoorientador);
        variables.put("emailCoorientador", emailCoorientador);
        variables.put("idDadosIniciais", idDadosIniciais);

        if (businessKey != null) {
            runtimeService.createMessageCorrelation("dadosAlunos")
                    .setVariables(variables)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            System.out.println("⚠️ SendMessageDelegate - Business Key está NULL! Tentando correlacionar sem Business Key...");
            runtimeService.createMessageCorrelation("dadosAlunos")
                    .setVariables(variables)
                    .correlateAll();
        }
    }
}
