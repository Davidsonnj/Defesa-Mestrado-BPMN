package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.documentos.services.*;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;

public class GerarDocumentosDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        // --- Coleta de Variáveis ---
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

        String membroInterno = "";
        String membroExterno = "";
        if (bancaList != null) {
            if (bancaList.size() > 0) {
                membroInterno = bancaList.get(0).getNome();
            }
            if (bancaList.size() > 1) {
                membroExterno = bancaList.get(1).getNome();
            }
        }

        // --- Geração de Documentos com Tratamento de Erro ---
        List<String> caminhosDosAnexos = new ArrayList<>();
        boolean todosGeradosComSucesso = true;

        try {
            caminhosDosAnexos.add(GeradorDeAta.gerarAta(
                    dataDefesa, horaDefesa, localDefesa, nomeAluno, tituloTese,
                    orientadorPrincipal, coorientador, membroInterno, membroExterno
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            System.err.println("Falha ao gerar a Ata de Defesa.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeFolhaDeAprovação.gerarFolhaDeAprovacao(
                    dataDefesa, nomeAluno, tituloTese, orientadorPrincipal,
                    coorientador, membroInterno, membroExterno
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            System.err.println("Falha ao gerar a Folha de Aprovação.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeDeclaracaoPrincipal.gerarDeclaracao(
                    nomeCoordenador, orientadorPrincipal, nomeAluno, tituloTese, dataDefesa
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            System.err.println("Falha ao gerar a Declaração do Orientador Principal.");
            e.printStackTrace();
        }

        try {
            caminhosDosAnexos.add(GeradorDeDeclaracaoCoorientador.gerarDeclaracao(
                    nomeCoordenador, coorientador, nomeAluno, tituloTese, dataDefesa
            ));
        } catch (Exception e) {
            todosGeradosComSucesso = false;
            System.err.println("Falha ao gerar a Declaração do Coorientador.");
            e.printStackTrace();
        }

        // Repita o padrão try-catch para os outros geradores (membro interno/externo)

        // --- Envio de E-mail ---
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
            System.err.println("Nenhum documento foi gerado com sucesso. E-mail não será enviado.");
        }

        if (todosGeradosComSucesso) {
            System.out.println("Todos os documentos foram gerados com sucesso!");
        } else {
            System.out.println("Processo de geração de documentos finalizado, mas com falhas. Verifique o log de erros.");
        }
    }
}