package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnviarDissertaçãoBancaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SenderEmailController emailSender = new SenderEmailController();

        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String caminhosDosAnexos = (String) execution.getVariable("caminhosDosAnexos");

        List<String> paths = (caminhosDosAnexos != null && !caminhosDosAnexos.isBlank())
                ? Arrays.asList(caminhosDosAnexos.split(";"))
                : new ArrayList<>();

        List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");

        for (Banca banca : bancaList) {
            String subject = "Dissertação do(a) aluno(a): " + aluno;
            String body = "Prezado(a) " + banca.getNome() + ",\n\n" +
                    "Informamos que a dissertação intitulada \"" + titulo_trabalho + "\" está anexada a este e-mail para sua apreciação.\n\n" +
                    "Caso haja qualquer dúvida ou necessidade de informações adicionais, por favor, não hesite em nos contatar.\n\n" +
                    "Atenciosamente,\n\n" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                    "IFES – Campus Serra";


            emailSender.sendEmail(banca.getEmail(), subject, body, paths);
        }
        System.out.println("Notificou todos os integrantes da banca!");
    }
}
