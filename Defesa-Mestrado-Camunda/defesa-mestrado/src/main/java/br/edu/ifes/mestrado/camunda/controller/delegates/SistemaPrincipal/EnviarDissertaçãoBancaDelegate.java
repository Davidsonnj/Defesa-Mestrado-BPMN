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


        Object rawBanca = execution.getVariable("bancaDefesa");
        System.out.println(rawBanca);

        List<Map<String, Object>> bancaMapList = (List<Map<String, Object>>) rawBanca;
        List<Banca> bancaList = new ArrayList<>();

        for (Map<String, Object> bancaMap : bancaMapList) {
            String nome = (String) bancaMap.get("nome");
            String email = (String) bancaMap.get("email");
            String instituicao = (String) bancaMap.get("instituicao");
            String minicurriculo = (String) bancaMap.get("minicurriculo");
            System.out.println(email);

            Banca banca = new Banca(nome, email, instituicao, minicurriculo);
            bancaList.add(banca);
        }

        for (Banca banca : bancaList) {
            String subject = "Dissertação do aluno: " + aluno;
            String body = "Prezado(a) " + banca.getNome() + ",\n\n"
                    + "Gostaríamos de informar que a defesa do trabalho, intitulada '"
                    + titulo_trabalho + "' está anexado neste email.\n\n"
                    + "Caso haja alguma dúvida ou se necessitar de mais informações, "
                    + "por favor, entre em contato conosco.\n\n"
                    + "Atenciosamente,\n"
                    + "PPComp";

            emailSender.sendEmail(banca.getEmail(), subject, body, paths);
        }
        System.out.println("Notificou todos os integrantes da banca!");
    }
}
