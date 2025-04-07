package br.edu.ifes.mestrado.camunda.controller.delegates.Orientador;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class EnviarDadosDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        String businessKey = execution.getProcessBusinessKey();

        Object bancaDefesa = execution.getVariable("bancaDefesa"); // Como é uma lista de objetos, será necessário fazer o cast correto
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");

        long count = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .messageEventSubscriptionName("CompletarCadastro")
                .count();
        System.out.println("Bussiness Key: " + businessKey + " Envio de dados do aluno");
        if (count > 0){
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("bancaDefesa", bancaDefesa);
            variables.put("dataDefesa", dataDefesa);
            variables.put("horaDefesa", horaDefesa);
            variables.put("localDefesa", localDefesa);

            runtimeService.createMessageCorrelation("CompletarCadastro")
                    .setVariables(variables)
                    .processInstanceBusinessKey(businessKey)
                    .correlate();
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem Envio de dados do aluno.");
        }
    }
}
