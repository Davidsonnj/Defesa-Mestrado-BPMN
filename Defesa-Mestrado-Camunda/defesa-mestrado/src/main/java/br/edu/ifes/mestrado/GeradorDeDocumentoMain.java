package br.edu.ifes.mestrado;

import br.edu.ifes.mestrado.documentos.services.*;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;

import java.util.ArrayList;
import java.util.List;

public class GeradorDeDocumentoMain {

    public static void main(String[] args) {

        SenderEmailController emailSender = new SenderEmailController();

        String nomeAluno = "Davidson Ca. Santos";
        String tituloTese = "Documento Teste de Geração de Documentos de Defesa";
        String dataDefesa = "01/10/2025";
        String horaDefesa = "18:07";
        String localDefesa = "Sala902T";
        String orientadorPrincipal = "Prof. Dr. Orientador Principal";
        String coOrientador = "Prof. Dr. Coorientador Teste";
        String membroInterno = "Prof. Dr. Membro Interno";
        String membroExterno = "Profª. Drª. Membro Externo";
        String nomeCoordenador = "Prof. Dr. Coordenador do PPComp"; // Adicionado para as declarações

        System.out.println("Iniciando a geração de documentos...");

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

        emailSender.sendEmail("davidsonifes@gmail.com", "Documentos de Defesa Gerados com Sucesso",
                "Prezado(a),\n\nOs documentos referentes à defesa do trabalho de " + nomeAluno + " foram gerados com sucesso. Seguem os anexos.\n\nAtenciosamente,\nSistema de Geração de Documentos",
                caminhosDosAnexos);

        System.out.println("\nProcesso de geração de documentos finalizado com sucesso!");
    }
}