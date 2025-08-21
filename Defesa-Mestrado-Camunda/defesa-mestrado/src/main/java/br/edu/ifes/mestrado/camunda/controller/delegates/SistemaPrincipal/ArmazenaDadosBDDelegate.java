package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.*;
import br.edu.ifes.mestrado.camunda.exception.ErroInsercaoBancoException;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.camunda.model.Aluno;
import br.edu.ifes.mestrado.camunda.model.Defesa;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmazenaDadosBDDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String nomeAluno = execution.getVariable("aluno").toString();
            String emailAluno = execution.getVariable("emailAluno").toString();

            Aluno aluno = new Aluno(nomeAluno, emailAluno);

            AlunoDAO alunoDAO = new AlunoDAO();
            int idAluno = alunoDAO.inserir(aluno);

            if (idAluno < 0) {
                throw new ErroInsercaoBancoException("Falha ao inserir aluno no banco de dados.");
            }

            String dataDefesa = execution.getVariable("dataDefesa").toString();
            String horaDefesa = execution.getVariable("horaDefesa").toString();
            String localDefesa = execution.getVariable("localDefesa").toString();
            String tituloTrabalho = execution.getVariable("titulo_trabalho").toString();
            long idDadosIniciais = (long) execution.getVariable("idDadosIniciais");
            long idConfirmacaoDefesa =  (long) execution.getVariable("idConfirmacaoDefesa");

            Defesa defesa = new Defesa(idAluno, dataDefesa, horaDefesa, localDefesa, tituloTrabalho);

            DefesaDAO defesaDAO = new DefesaDAO();
            int idDefesa = defesaDAO.inserir(defesa);

            DefesaEmailsDAO defesaEmailsDAO = new DefesaEmailsDAO();
            defesaEmailsDAO.inserir(idDefesa, idDadosIniciais);
            defesaEmailsDAO.inserir(idDefesa, idConfirmacaoDefesa);

            List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");

            List<Integer> idBancaList = new ArrayList<>();
            BancaDAO bancaDAO = new BancaDAO();
            for (Banca banca : bancaList) {
                int idBanca = bancaDAO.inserir(banca);
                idBancaList.add(idBanca);
            }

            DefesaBancaDAO defesaBanca = new DefesaBancaDAO();
            for (int idBanca : idBancaList) {
                defesaBanca.inserir(idDefesa, idBanca);
            }

            execution.setVariable("idDefesaBD", idDefesa);

        } catch (ErroInsercaoBancoException e) {
            System.err.println("Erro ao armazenar dados: " + e.getMessage());
            execution.setVariable("erroArmazenamento", true);
            throw e;
        }
    }
}