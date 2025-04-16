package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DefesaAutorizadaCoordenacaoDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution){

        System.out.println("Chegou em 'Defesa Autorizada' no BPMN");

    }
}
