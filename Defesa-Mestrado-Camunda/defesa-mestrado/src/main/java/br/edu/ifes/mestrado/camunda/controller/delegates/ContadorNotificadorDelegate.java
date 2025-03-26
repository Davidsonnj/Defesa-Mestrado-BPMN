package br.edu.ifes.mestrado.camunda.controller.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ContadorNotificadorDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if(execution.hasVariable("contadorDeNotificacao")) {
            int contador = (int) execution.getVariable("contadorDeNotificacao");
            contador++;
            execution.setVariable("contadorDeNotificacao", contador);
        } else {
            execution.setVariable("contadorDeNotificacao", 2);
        }
    }

}
