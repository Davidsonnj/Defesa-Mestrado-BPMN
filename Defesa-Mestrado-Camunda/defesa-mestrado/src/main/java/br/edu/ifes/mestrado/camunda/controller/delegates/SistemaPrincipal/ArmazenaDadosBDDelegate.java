package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.*;
import br.edu.ifes.mestrado.database.dao.implementations.*;
import br.edu.ifes.mestrado.camunda.exception.ErroInsercaoBancoException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmazenaDadosBDDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String dataDefesa = execution.getVariable("dataDefesa").toString();
            String horaDefesa = execution.getVariable("horaDefesa").toString();
            String localDefesa = execution.getVariable("localDefesa").toString();
            String tituloTrabalho = execution.getVariable("titulo_trabalho").toString();
            long idDadosIniciais = (long) execution.getVariable("idDadosIniciais");
            long idConfirmacaoDefesa =  (long) execution.getVariable("idConfirmacaoDefesa");

            String nomeAluno = execution.getVariable("aluno").toString();
            String emailAluno = execution.getVariable("emailAluno").toString();

            Aluno aluno = new Aluno(nomeAluno, emailAluno);

            AlunoDAO alunoDAO = new AlunoDAO();
            int idAluno = alunoDAO.inserir(aluno);

            if (idAluno < 0) {
                throw new ErroInsercaoBancoException("Falha ao inserir aluno no banco de dados.");
            }

            String nomeOrientador =  execution.getVariable("nomeOrientador").toString();
            String emailOrientador =  execution.getVariable("emailOrientador").toString();

            Orientador orientador= new Orientador(nomeOrientador, emailOrientador);

            OrientadorDAO orientadorDAO = new OrientadorDAO();
            int idOrientador = orientadorDAO.inserir(orientador);

            if (idOrientador < 0) {
                throw new ErroInsercaoBancoException("Falha ao inserir aluno no banco de dados.");
            }

            String nomeCoorientador =  execution.getVariable("nomeCoorientador").toString();
            String emailCoorientador =  execution.getVariable("emailCoorientador").toString();

            Coorientador coorientador = new Coorientador(nomeCoorientador, emailCoorientador);

            CoorientadorDAO coorientadorDAO = new CoorientadorDAO();
            int idCoorientador = coorientadorDAO.inserir(coorientador);

            if (idCoorientador < 0) {
                throw new ErroInsercaoBancoException("Falha ao inserir aluno no banco de dados.");
            }

            Defesa defesa = new Defesa(idAluno, idOrientador, idCoorientador, dataDefesa, horaDefesa, localDefesa, tituloTrabalho);

            DefesaDAO defesaDAO = new DefesaDAO();
            int idDefesa = defesaDAO.inserir(defesa);

            // Relacionamento dos email com a defesa
            DefesaEmailsDAO defesaEmailsDAO = new DefesaEmailsDAO();
            defesaEmailsDAO.inserir(idDefesa, idDadosIniciais);
            defesaEmailsDAO.inserir(idDefesa, idConfirmacaoDefesa);

            // Adiciona banca no BD
            List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");

            List<Integer> idBancaList = new ArrayList<>();
            BancaDAO bancaDAO = new BancaDAO();
            for (Banca banca : bancaList) {
                int idBanca = bancaDAO.inserir(banca);
                idBancaList.add(idBanca);
            }

            // Relacionamento da banca com a defesa
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