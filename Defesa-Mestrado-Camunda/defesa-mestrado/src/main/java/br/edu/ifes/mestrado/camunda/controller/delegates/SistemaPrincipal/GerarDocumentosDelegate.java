package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.documentos.services.*;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import br.edu.ifes.mestrado.emailAPI.service.EmailSenderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;

public class GerarDocumentosDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){

        SenderEmailController emailSender = new SenderEmailController();

        String nomeAluno = (String) execution.getVariable("aluno");
        String tituloTese = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String orientadorPrincipal = (String) execution.getVariable("nomeOrientador");
        String coOrientador = "Prof. Dr. Coorientador Teste";
        String membroInterno = "Prof. Dr. Membro Interno";
        String membroExterno = "Profª. Drª. Membro Externo";
        String nomeCoordenador = "Prof. Dr. Coordenador do PPComp";

        List<String> caminhosDosAnexos = new ArrayList<>();

        caminhosDosAnexos.add(GeradorDeAta.gerarAta(
                dataDefesa,
                horaDefesa,
                localDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coOrientador,
                membroInterno,
                membroExterno
        ));

        caminhosDosAnexos.add(GeradorDeFolhaDeAprovação.gerarFolhaDeAprovacao(
                dataDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coOrientador,
                membroInterno,
                membroExterno
        ));

        caminhosDosAnexos.add(GeradorDeDeclaracaoPrincipal.gerarDeclaracao(
                nomeCoordenador,
                orientadorPrincipal,
                nomeAluno,
                tituloTese,
                dataDefesa
        ));

        caminhosDosAnexos.add(GeradorDeDeclaracaoCoorientador.gerarDeclaracao(
                nomeCoordenador,
                coOrientador,
                nomeAluno,
                tituloTese,
                dataDefesa
        ));

        caminhosDosAnexos.add(GeradorDeDeclaracaoMembroInterno.gerarDeclaracao(
                nomeCoordenador,
                membroInterno,
                nomeAluno,
                tituloTese,
                dataDefesa
        ));

        caminhosDosAnexos.add(GeradorDeDeclaracaoMembroExterno.gerarDeclaracao(
                nomeCoordenador,
                membroExterno,
                nomeAluno,
                tituloTese,
                dataDefesa
        ));

        System.out.println("Todos os documentos foram gerados com sucesso!");


    }
}
