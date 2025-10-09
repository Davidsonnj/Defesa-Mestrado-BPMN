package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.VinculacaoAluno;
import br.edu.ifes.mestrado.database.dao.implementations.VinculacaoDAO;
import br.edu.ifes.mestrado.camunda.exception.ErroInsercaoBancoException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VinculacaoArmazenaDadosBDDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(VinculacaoArmazenaDadosBDDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Iniciando armazenamento de dados da vinculação...");

        try {
            String alunoNome = (String) execution.getVariable("alunoNome");
            String matricula = (String) execution.getVariable("matricula");
            String emailAluno = (String) execution.getVariable("emailAluno");
            String tema = (String) execution.getVariable("tema");
            String periodoInicio = (String) execution.getVariable("periodoInicio");
            Long idEmail = (Long) execution.getVariable("idEmail");

            VinculacaoAluno vinculacao = new VinculacaoAluno();
            vinculacao.setAlunoNome(alunoNome);
            vinculacao.setMatricula(matricula);
            vinculacao.setEmailAluno(emailAluno);
            vinculacao.setTema(tema);
            vinculacao.setPeriodoInicio(periodoInicio);
            vinculacao.setEmailId(idEmail);

            VinculacaoDAO vinculacaoDAO = new VinculacaoDAO();
            vinculacaoDAO.insert(vinculacao);

        } catch (Exception e) {
            String mensagemErro = "Ocorreu um erro ao armazenar os dados da vinculação no banco de dados.";
            LOGGER.error(mensagemErro, e);
            execution.setVariable("erroArmazenamentoVinculacao", true);
            execution.setVariable("mensagemErroArmazenamento", e.getMessage());
            throw e;
        }
    }
}