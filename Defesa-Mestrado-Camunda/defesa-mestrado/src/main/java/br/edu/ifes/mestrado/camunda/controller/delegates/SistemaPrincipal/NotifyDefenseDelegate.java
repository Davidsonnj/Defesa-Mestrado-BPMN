package br.edu.ifes.mestrado.camunda.controller.delegates.SistemaPrincipal;

import br.edu.ifes.mestrado.database.dao.implementations.DefesaDAO;
import br.edu.ifes.mestrado.emailAPI.controller.SenderEmailController;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyDefenseDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyDefenseDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        DefesaDAO defesaDAO = new DefesaDAO();
        SenderEmailController emailSender = new SenderEmailController();

        String businessKey = execution.getProcessBusinessKey();

        String aluno = (String) execution.getVariable("aluno");
        String tituloTrabalho = (String) execution.getVariable("titulo_trabalho");
        String emailAluno = (String) execution.getVariable("emailAluno");
        String emailOrientador = (String) execution.getVariable("emailOrientador");
        String dataDefesa = (String) execution.getVariable("dataDefesa");
        String horaDefesa = (String) execution.getVariable("horaDefesa");
        String localDefesa = (String) execution.getVariable("localDefesa");
        String tipoDefesa = (String) execution.getVariable("tipoDefesa");
        int idDefesa = (Integer) execution.getVariable("idDefesaBD");

        defesaDAO.atualizarStatus(idDefesa, "NotificaAluno");

        String subject;
        String body;

        if (tipoDefesa.equals("qualificacao")){
            subject = "Informações sobre o Exame de Qualificação de " + aluno;
            body = "Prezado(a) " + aluno + ",<br><br>" +
                    "Informamos que a seu exame de qualificação, intitulada &quot;" + tituloTrabalho + "&quot;, está agendada conforme os detalhes abaixo:<br><br>" +

                    "Título do Trabalho: " + tituloTrabalho + "<br>" +
                    "Aluno(a): " + aluno + "<br><br>" +

                    "Data da Defesa: " + dataDefesa + "<br>" +
                    "Hora da Defesa: " + horaDefesa + "<br>" +
                    "Local da Defesa: " + localDefesa + "<br><br>" +

                    "Solicitamos, por gentileza, que <strong>confirme sua participação</strong> respondendo a este e-mail (ex: 'Confirmo a minha presença', 'Não participarei').<br><br>" +
                    "Ressaltamos que este pedido de confirmação será enviado por até três dias consecutivos. Caso não confirme dentro do prazo, o processo será cancelado.<br><br>" +
                    "A defesa será realizada conforme o cronograma e local previamente definidos.<br><br>" +
                    "Caso tenha alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES – Campus Serra";
        }else {
            subject = "Informações sobre a Defesa de Trabalho de " + aluno;
            body = "Prezado(a) " + aluno + ",<br><br>" +
                    "Informamos que a sua defesa de trabalho, intitulada &quot;" + tituloTrabalho + "&quot;, está agendada conforme os detalhes abaixo:<br><br>" +

                    "Título do Trabalho: " + tituloTrabalho + "<br>" +
                    "Aluno(a): " + aluno + "<br><br>" +

                    "Data da Defesa: " + dataDefesa + "<br>" +
                    "Hora da Defesa: " + horaDefesa + "<br>" +
                    "Local da Defesa: " + localDefesa + "<br><br>" +

                    "Solicitamos, por gentileza, que <strong>confirme sua participação</strong> respondendo a este e-mail (ex: 'Confirmo a minha presença', 'Não participarei').<br><br>" +
                    "Ressaltamos que este pedido de confirmação será enviado por até três dias consecutivos. Caso não confirme dentro do prazo, o processo será cancelado.<br><br>" +
                    "A defesa será realizada conforme o cronograma e local previamente definidos.<br><br>" +
                    "Caso tenha alguma dúvida ou necessite de informações adicionais, por favor, não hesite em nos contatar.<br><br>" +
                    "Atenciosamente,<br><br>" +
                    "Programa de Pós-Graduação em Computação Aplicada (PPComp)<br>" +
                    "IFES – Campus Serra";
        }



        emailSender.sendEmail(emailAluno, subject, body);

        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        List<Execution> execucoes = runtimeService.createExecutionQuery()
                .processDefinitionKey("Process_1eeteym")
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

        System.out.println(execucoes);

        if (execucoes.isEmpty()) {
            if (businessKey != null) {

                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("aluno", aluno);
                variables.put("titulo_trabalho", tituloTrabalho);
                variables.put("emailAluno", emailAluno);
                variables.put("emailOrientador", emailOrientador);
                variables.put("tipoDefesa", tipoDefesa);


                runtimeService.createMessageCorrelation("confirmarDefesaMessage")
                        .setVariables(variables)
                        .processInstanceBusinessKey(businessKey)
                        .correlate();
            } else {
                LOGGER.info("NotifyDefense - Business Key está NULL!");
            }
        } else {
            LOGGER.warn("Processo de confirmação da defesa já existe para o aluno: " + aluno);
        }
    }
}
