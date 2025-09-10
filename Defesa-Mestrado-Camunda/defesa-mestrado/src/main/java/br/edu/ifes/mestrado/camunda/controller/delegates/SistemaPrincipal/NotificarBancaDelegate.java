package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.camunda.model.Banca;
import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificarBancaDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificarBancaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        SenderEmailController emailSender = new SenderEmailController();
        DefesaDAO defesaDAO = new DefesaDAO();

        String aluno = (String) execution.getVariable("aluno");
        String titulo_trabalho = (String) execution.getVariable("titulo_trabalho");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String nomeOrientador = (String) execution.getVariable("nomeOrientador");
        String nomeCoorientador = (String) execution.getVariable("nomeCoorientador");
        String emailCoorientador = (String) execution.getVariable("emailCoorientador");
        int idDefesa = (int) execution.getVariable("idDefesaBD");

        List<Banca> bancaList = (List<Banca>) execution.getVariable("bancaDefesa");
        LOGGER.info("Variável 'bancaDefesa' lida com sucesso. Conteúdo: " + bancaList);

        String subject = "Informações sobre a Defesa de Trabalho de " + aluno;

        for (Banca banca : bancaList) {
            String body = gerarCorpoEmail(banca.getNome(), titulo_trabalho, aluno, dataDefesa, horaDefesa, localDefesa);
            emailSender.sendEmail(banca.getEmail(), subject, body);
        }

        String bodyOrientador = gerarCorpoEmail(nomeOrientador, titulo_trabalho, aluno, dataDefesa, horaDefesa, localDefesa);
        emailSender.sendEmail(emailOrientador, subject, bodyOrientador);

        if (nomeCoorientador != null && !nomeCoorientador.isBlank() &&
                emailCoorientador != null && !emailCoorientador.isBlank()) {

            String bodyCoorientador = gerarCorpoEmail(nomeOrientador, titulo_trabalho, aluno, dataDefesa, horaDefesa, localDefesa);
            emailSender.sendEmail(emailCoorientador, subject, bodyCoorientador);

        }

        defesaDAO.atualizarStatus(idDefesa, "NotificaBanca");

        LOGGER.info("Notificou todos os integrantes da banca sobre o horário e local de defesa.");
    }

    private String gerarCorpoEmail(String nomeDestinatario, String tituloTrabalho, String aluno, String data, String hora, String local) {
        return "Prezado(a) " + nomeDestinatario + ",<br><br>" +
                 "Informamos que a defesa do trabalho, intitulada &quot;" + tituloTrabalho + "&quot;, está agendada conforme os detalhes abaixo:<br><br>" +
                "Título do Trabalho: " + tituloTrabalho + "<br>" +
                "Aluno(a): " + aluno + "<br>" +
                "Data: " + data + "<br>" +
                "Hora: " + hora + "<br>" +
                "Local: " + local + "<br><br>" +
                "A defesa ocorrerá conforme o cronograma e local previamente definidos.<br><br>" +
                "Caso haja alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.<br><br>" +
                "Atenciosamente,<br><br>" +
                "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                "Instituto Federal do Espírito Santo – Campus Serra";
    }
}
