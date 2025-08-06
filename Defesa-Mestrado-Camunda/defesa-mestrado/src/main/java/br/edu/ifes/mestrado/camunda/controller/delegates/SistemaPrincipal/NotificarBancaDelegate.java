package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.List;

public class NotificarBancaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        SenderEmailController emailSender = new SenderEmailController();

        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String nomeOrientador = (String) execution.getVariable("nomeOrientador");

        List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");
        System.out.println("Variável 'bancaDefesa' lida com sucesso. Conteúdo: " + bancaList);

        String subject = "Informações sobre a Defesa de Trabalho de " + aluno;

        for (Banca banca : bancaList) {
            String body = gerarCorpoEmail(banca.getNome(), titulo_trabalho, aluno, dataDefesa, horaDefesa, localDefesa);
            emailSender.sendEmail(banca.getEmail(), subject, body);
        }

        String bodyOrientador = gerarCorpoEmail(nomeOrientador, titulo_trabalho, aluno, dataDefesa, horaDefesa, localDefesa);
        emailSender.sendEmail(emailOrientador, subject, bodyOrientador);

        System.out.println("Notificou todos os integrantes da banca sobre o horário e local de defesa.");
    }

    private String gerarCorpoEmail(String nomeDestinatario, String tituloTrabalho, String aluno, String data, String hora, String local) {
        return "Prezado(a) " + nomeDestinatario + ",\n\n" +
                "Informamos que a defesa do trabalho, intitulada \"" + tituloTrabalho + "\", está agendada conforme os detalhes abaixo:\n\n" +
                "Título do Trabalho: " + tituloTrabalho + "\n" +
                "Aluno(a): " + aluno + "\n" +
                "Data: " + data + "\n" +
                "Hora: " + hora + "\n" +
                "Local: " + local + "\n\n" +
                "A defesa ocorrerá conforme o cronograma e local previamente definidos.\n\n" +
                "Caso haja alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.\n\n" +
                "Atenciosamente,\n\n" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)\n" +
                "Instituto Federal do Espírito Santo – Campus Serra";
    }
}
