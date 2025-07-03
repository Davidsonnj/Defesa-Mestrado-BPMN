package br.edu.ifes.mestrado;

import br.edu.ifes.mestrado.documentos.services.*;

public class GeradorDeDocumentoMain {

    public static void main(String[] args) {

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

        GeradorDeAta.gerarAta(
                dataDefesa,
                horaDefesa,
                localDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coOrientador,
                membroInterno,
                membroExterno
        );

        GeradorDeFolhaDeAprovação.gerarFolhaDeAprovacao(
                dataDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coOrientador,
                membroInterno,
                membroExterno
        );

        GeradorDeDeclaracaoPrincipal.gerarDeclaracao(
                nomeCoordenador,
                orientadorPrincipal,
                nomeAluno,
                tituloTese,
                dataDefesa
        );

        GeradorDeDeclaracaoCoorientador.gerarDeclaracao(
                nomeCoordenador,
                coOrientador,
                nomeAluno,
                tituloTese,
                dataDefesa
        );

        GeradorDeDeclaracaoMembroInterno.gerarDeclaracao(
                nomeCoordenador,
                membroInterno,
                nomeAluno,
                tituloTese,
                dataDefesa
        );

        GeradorDeDeclaracaoMembroExterno.gerarDeclaracao(
                nomeCoordenador,
                membroExterno,
                nomeAluno,
                tituloTese,
                dataDefesa
        );

        System.out.println("\nProcesso de geração de documentos finalizado com sucesso!");
    }
}