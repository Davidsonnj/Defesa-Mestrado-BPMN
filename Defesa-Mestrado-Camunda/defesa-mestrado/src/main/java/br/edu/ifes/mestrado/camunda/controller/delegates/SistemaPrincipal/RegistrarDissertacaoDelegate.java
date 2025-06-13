package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DissertacaoDAO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class RegistrarDissertacaoDelegate implements JavaDelegate {
    public void execute(DelegateExecution execution) throws Exception {
        int idDefesa = (int) execution.getVariable("idDefesaBD");
        String caminhosDosAnexos = (String) execution.getVariable("caminhosDosAnexos");

        DissertacaoDAO dissertacaoDAO = new DissertacaoDAO();
        dissertacaoDAO.inserir(idDefesa, caminhosDosAnexos);
        System.out.println("Dissertacao registrada com sucesso!");
    }
}
