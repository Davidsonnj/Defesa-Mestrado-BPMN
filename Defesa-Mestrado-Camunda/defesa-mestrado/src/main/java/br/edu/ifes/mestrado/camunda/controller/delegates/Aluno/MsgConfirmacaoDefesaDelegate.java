package br.edu.ifes.mestrado.camunda.controller.delegates.Aluno;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class MsgConfirmacaoDefesaDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        System.out.println("Tentando correlacionar mensagem para BusinessKey: " + businessKey);

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
            System.out.println("Mensagem correlacionada com sucesso.");
        } else {
            System.out.println("Nenhuma instância encontrada esperando pela mensagem.");
        }

    }

}
