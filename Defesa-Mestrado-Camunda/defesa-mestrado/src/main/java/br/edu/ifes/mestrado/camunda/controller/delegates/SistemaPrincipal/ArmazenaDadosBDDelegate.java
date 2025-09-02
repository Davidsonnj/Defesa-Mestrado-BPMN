package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.controller.delegates.Aluno.MsgConfirmacaoDefesaDelegate;
import br.edu.ifes.mestrado.camunda.model.*;
import br.edu.ifes.mestrado.database.dao.implementations.*;
import br.edu.ifes.mestrado.camunda.exception.ErroInsercaoBancoException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmazenaDadosBDDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgConfirmacaoDefesaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            int idDefesa = (int) execution.getVariable("idDefesaBD");
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

            String nomeCoorientador = (String) execution.getVariable("nomeCoorientador");
            String emailCoorientador = (String) execution.getVariable("emailCoorientador");

            int idCoorientador = -1;

            if (nomeCoorientador != null && !nomeCoorientador.isBlank() &&
                    emailCoorientador != null && !emailCoorientador.isBlank()) {

                Coorientador coorientador = new Coorientador(nomeCoorientador, emailCoorientador);
                CoorientadorDAO coorientadorDAO = new CoorientadorDAO();
                idCoorientador = coorientadorDAO.inserir(coorientador);

                if (idCoorientador < 0) {
                    throw new ErroInsercaoBancoException("Falha ao inserir coorientador no banco de dados.");
                }
            }

            Defesa defesa = new Defesa(idAluno, idOrientador, idCoorientador, dataDefesa, horaDefesa, localDefesa, tituloTrabalho);

            DefesaDAO defesaDAO = new DefesaDAO();
            defesaDAO.atualizar(idDefesa, defesa);

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


        } catch (ErroInsercaoBancoException e) {
            LOGGER.error("Erro ao armazenar dados: {}", e.getMessage());
            execution.setVariable("erroArmazenamento", true);
            throw e;
        }
    }
}