package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EnviarJustificativaAnuenciaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        System.out.println("Chegou em 'Jusitifcativa de defesa negada reportada' no BPMN");

    }
}
