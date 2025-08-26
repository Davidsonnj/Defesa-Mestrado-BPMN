package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;


import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class StartProcess implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        DefesaDAO defesaDAO = new DefesaDAO();

        int idDefesa = defesaDAO.criarInstanciaVazia();

        execution.setVariable("idDefesaBD", idDefesa);
    }
}

