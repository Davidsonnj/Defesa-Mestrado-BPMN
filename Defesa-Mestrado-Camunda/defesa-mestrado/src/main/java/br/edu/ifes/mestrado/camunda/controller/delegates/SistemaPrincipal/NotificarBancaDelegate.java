package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificarBancaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution){
        SenderEmailController emailSender = new SenderEmailController();

        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa =  (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");

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
            String subject = "Informações sobre a Defesa de Trabalho de " + aluno;
            String body = "Prezado(a) " + banca.getNome() + ",\n\n" +
                    "Informamos que a defesa do trabalho, intitulada \"" + titulo_trabalho + "\", está agendada conforme os detalhes abaixo:\n\n" +
                    "Título do Trabalho: " + titulo_trabalho + "\n" +
                    "Aluno(a): " + aluno + "\n" +
                    "Data: " + dataDefesa + "\n" +
                    "Hora: " + horaDefesa + "\n" +
                    "Local: " + localDefesa + "\n\n" +
                    "A defesa ocorrerá conforme o cronograma e local previamente definidos.\n\n" +
                    "Caso haja alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.\n\n" +
                    "Atenciosamente,\n\n" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                    "Instituto Federal do Espírito Santo – Campus Serra";

            emailSender.sendEmail(banca.getEmail(), subject, body);
        }
        System.out.println("Notificou todos os integrantes da banca sobre o horario e local de defesa.");

    }
}
