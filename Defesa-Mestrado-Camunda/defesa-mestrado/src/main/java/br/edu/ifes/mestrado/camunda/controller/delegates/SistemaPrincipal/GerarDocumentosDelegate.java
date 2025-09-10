package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaMembroExterno;
import br.edu.ifes.mestrado.GenAI.pergunta.implementacoes.PerguntaValidacaoDoc;
import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.camunda.model.BancaMembro;
import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import br.edu.ifes.mestrado.documentos.services.*;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GerarDocumentosDelegate implements JavaDelegate {

    @Autowired
    PerguntaMembroExterno perguntaMembroExterno;

    private static final Logger LOGGER = LoggerFactory.getLogger(GerarDocumentosDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        int idDefesa = (Integer) execution.getVariable("idDefesaBD");

        String emailOrientador = (String) execution.getVariable("emailOrientador");
        List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");

        String nomeAluno = (String) execution.getVariable("aluno");
        String tituloTese = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String orientadorPrincipal = (String) execution.getVariable("nomeOrientador");
        String coorientador = (String) execution.getVariable("nomeCoorientador");
        String nomeCoordenador = "Profª. Drª. Karin Satie Komati";

        // Tratamento de null para todas as variáveis de string
        nomeAluno = (nomeAluno != null) ? nomeAluno : "";
        tituloTese = (tituloTese != null) ? tituloTese : "";
        dataDefesa = (dataDefesa != null) ? dataDefesa : "";
        horaDefesa = (horaDefesa != null) ? horaDefesa : "";
        localDefesa = (localDefesa != null) ? localDefesa : "";
        orientadorPrincipal = (orientadorPrincipal != null) ? orientadorPrincipal : "";
        coorientador = (coorientador != null) ? coorientador : "";
        nomeCoordenador = (nomeCoordenador != null) ? nomeCoordenador : "";

        String membroInterno = "";
        String membroExterno = "";

        if (bancaList != null && !bancaList.isEmpty()) {

            List<BancaMembro> bancaMembros = new ArrayList<>();

            for (Banca membro : bancaList) {
                BancaMembro bancaMembro = new BancaMembro(membro, false);
                bancaMembros.add(bancaMembro);
            }

            for (BancaMembro bancaMembro : bancaMembros) {
                perguntaMembroExterno.takeQuestionMembroExterno(bancaMembro);
                if (bancaMembro.isExterno()){
                    membroExterno = bancaMembro.getNome();
                }else{
                    membroInterno = bancaMembro.getNome();
                }
            }

        }

        // geração de documentos com tratamento de erro
        List<String> caminhosDosAnexos = new ArrayList<>();
        boolean todosGeradosComSucesso = true;

        try {
            caminhosDosAnexos.add(GeradorDeAta.gerarAta(
                    dataDefesa, horaDefesa, localDefesa, nomeAluno, tituloTese,
                    orientadorPrincipal, coorientador, membroInterno, membroExterno
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            LOGGER.error("Falha ao gerar a Ata de Defesa.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeFolhaDeAprovação.gerarFolhaDeAprovacao(
                    dataDefesa, nomeAluno, tituloTese, orientadorPrincipal,
                    coorientador, membroInterno, membroExterno
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            LOGGER.error("Falha ao gerar a Folha de Aprovação.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeDeclaracaoPrincipal.gerarDeclaracao(
                    nomeCoordenador, orientadorPrincipal, nomeAluno, tituloTese, dataDefesa
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            LOGGER.error("Falha ao gerar a Declaração do Orientador Principal.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeDeclaracaoCoorientador.gerarDeclaracao(
                    nomeCoordenador, coorientador, nomeAluno, tituloTese, dataDefesa
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            LOGGER.error("Falha ao gerar a Declaração do Coorientador.");
            e.printStackTrace();
        }

        // Repitir o padrão try-catch para os outros geradores (membro interno/externo)

        if (!caminhosDosAnexos.isEmpty()) {
            SenderEmailController emailSender = new SenderEmailController();
            emailSender.sendEmail(
                    emailOrientador,
                    "Documentos de Defesa Gerados",
                    "Prezado(a) Professor(a),<br><br>" +
                            "Informamos que os documentos referentes à defesa do(a) aluno(a) " + nomeAluno + " foram gerados e estão anexados a este e-mail.<br><br>" +
                            "Caso algum documento esteja faltando, ocorreu um erro durante sua geração. Por favor, verifique o sistema ou contate o suporte.<br><br>" +
                            "Atenciosamente,<br>" +
                            "PPComp - IFES Serra",
                    caminhosDosAnexos
            );
        } else {
            LOGGER.error("Nenhum documento foi gerado com sucesso. E-mail não será enviado.");
        }

        if (todosGeradosComSucesso) {
            LOGGER.info("Todos os documentos foram gerados com sucesso!");
        } else {
            LOGGER.warn("Processo de geração de documentos finalizado, mas com falhas. Verifique o log de erros.");
        }

        DefesaDAO defesaDAO = new DefesaDAO();
        defesaDAO.atualizarStatus(idDefesa, "GeraDocumento");
    }
}