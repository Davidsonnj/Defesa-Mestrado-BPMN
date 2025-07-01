package br.edu.ifes.mestrado;

import br.edu.ifes.mestrado.documentos.services.GeradorDeAta;

public class GeradorDeDocumentoMain {

    public static void main(String[] args) {

        String nomeAluno = "Davidson Ca. Santos";
        String tituloTese = "Documento Teste de Geração de Ata de Defesa";
        String dataDefesa = "01/10/2025";
        String horaDefesa = "18:07";
        String localDefesa = "Sala902T";
        String orientadorPrincipal = "Prof. Dr. Orientador Principal";
        String coOrientador = "Prof. Dr. Coorientador Teste";
        String membroInterno = "Prof. Dr. Membro Interno";
        String membroExterno = "Profª. Drª. Membro Externo";

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

        System.out.println("Documento gerado com sucesso!");
    }
}