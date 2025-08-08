package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
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
        String membroInterno = "";
        String membroExterno = "";

        String emailOrientador = (String) execution.getVariable("emailOrientador");
        List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");

        String nomeAluno = (String) execution.getVariable("aluno");
        String tituloTese = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String orientadorPrincipal = (String) execution.getVariable("nomeOrientador");
        String coorientador = (String)  execution.getVariable("nomeCoorientador");
        for (int i = 0; i < Math.min(2, bancaList.size()); i++) {
            Banca banca = bancaList.get(i);

            if (i == 0) {
                membroInterno = "Prof. Dr. " + banca.getNome();
            } else if (i == 1) {
                membroExterno = "Prof. Dr. " + banca.getNome();
            }
        }

        String nomeCoordenador = "Profª. Drª. Karin Satie Komati"; //(Alterar o nome do coordenador, caso necessario. Futuramente pode haver um webscrapping aq)

        List<String> caminhosDosAnexos = new ArrayList<>();

        caminhosDosAnexos.add(GeradorDeAta.gerarAta(
                dataDefesa,
                horaDefesa,
                localDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coorientador,
                membroInterno,
                membroExterno
        ));

        caminhosDosAnexos.add(GeradorDeFolhaDeAprovação.gerarFolhaDeAprovacao(
                dataDefesa,
                nomeAluno,
                tituloTese,
                orientadorPrincipal,
                coorientador,
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
                coorientador,
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

        emailSender.sendEmail(
                emailOrientador,
                "Documentos de Defesa Gerados com Sucesso",
                "Prezado(a) Professor(a),<br><br>" +
                    "Informamos que os documentos referentes à defesa do(a) aluno(a) " + nomeAluno + " foram gerados com sucesso e estão anexados a este e-mail.<br><br>" +
                    "Solicitamos, por gentileza, que realize a conferência dos dados presentes nos documentos, especialmente:<br>" +
                    "<ul>" +
                    "<li>Nome do(a) aluno(a)</li>" +
                    "<li>Título do trabalho</li>" +
                    "<li>Data, horário e local da defesa</li>" +
                    "<li>Composição da banca examinadora (orientador, coorientador, membros interno e externo)</li>" +
                    "</ul><br>" +
                    "Caso identifique qualquer divergência ou informação incorreta, favor entrar em contato com a coordenação o mais breve possível para que as correções sejam efetuadas.<br><br>" +
                    "Atenciosamente,<br>" +
                    "PPComp - IFES Serra",
                caminhosDosAnexos
        );


        System.out.println("Todos os documentos foram gerados com sucesso!");


    }
}
