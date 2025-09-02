package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnviarDissertaçãoBancaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarDissertaçãoBancaDelegate.class);

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
            String body = "Prezado(a) " + banca.getNome() + ",<br><br>" +
                    "Informamos que a dissertação intitulada &quot;" + titulo_trabalho + "&quot; está anexada a este e-mail para sua apreciação.<br><br>" +
                    "Caso haja qualquer dúvida ou necessidade de informações adicionais, por favor, não hesite em nos contatar.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES – Campus Serra";



            emailSender.sendEmail(banca.getEmail(), subject, body, paths);
        }
        LOGGER.info("Notificou todos os integrantes da banca!");
    }
}
