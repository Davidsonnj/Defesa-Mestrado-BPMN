package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DissertacaoDAO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrarDissertacaoDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrarDissertacaoDelegate.class);

    public void execute(DelegateExecution execution) throws Exception {
        int idDefesa = (int) execution.getVariable("idDefesaBD");
        String caminhosDosAnexos = (String) execution.getVariable("caminhosDosAnexos");

        DissertacaoDAO dissertacaoDAO = new DissertacaoDAO();
        dissertacaoDAO.inserir(idDefesa, caminhosDosAnexos);
        LOGGER.info("Dissertacao registrada com sucesso!");
    }
}
