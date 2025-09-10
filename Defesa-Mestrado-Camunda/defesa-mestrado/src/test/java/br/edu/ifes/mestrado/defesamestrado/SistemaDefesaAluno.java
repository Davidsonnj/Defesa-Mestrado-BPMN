package br.edu.ifes.mestrado.defesamestrado;

import br.edu.ifes.mestrado.camunda.controller.delegates.Aluno.BuscarEmailDefesaDelegate;
import br.edu.ifes.mestrado.camunda.controller.delegates.Aluno.BuscarEmailDefesaDocDelegate;
import br.edu.ifes.mestrado.camunda.controller.delegates.Aluno.MsgConfirmacaoDefesaDelegate;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@SpringBootTest
public class SistemaDefesaAluno {

    @Autowired
    private RuntimeService runtimeService;

    @MockBean
    private MsgConfirmacaoDefesaDelegate msgConfirmacaoDefesaDelegate;
    @MockBean
    private BuscarEmailDefesaDelegate buscarEmailDefesaDelegate;
    @MockBean
    private BuscarEmailDefesaDocDelegate buscarEmailDefesaDocDelegate;


    @Test
    @Deployment(resources = {"BPMNs/Aluno.bpmn"})
    @Transactional
    public void testeCaminhoPrincipalCompleto() {
        System.out.println("INICIANDO TESTE COM RECARREGAMENTO DE INSTÂNCIA");

        Map<String, Object> variables = new HashMap<>();
        variables.put("aluno", "Davidson Silva");
        variables.put("titulo_trabalho", "Análise de Processos com BPMN e Camunda");
        variables.put("emailAluno", "davidsonifes@gmail.com");

        System.out.println("Iniciando a instância do processo...");
        String businessKey = "aluno-teste-sync-reload-" + System.currentTimeMillis();
        ProcessInstance processInstance = runtimeService.createMessageCorrelation("confirmarDefesaMessage")
                .processInstanceBusinessKey(businessKey)
                .setVariables(variables)
                .correlateWithResult().getProcessInstance();

        System.out.println("Verificando se o processo está parado em 'gateway_email_aluno'...");
        assertThat(processInstance).isNotNull().isActive();
        assertThat(processInstance).isWaitingAt("gateway_email_aluno");
        System.out.println("Processo está corretamente esperando em 'gateway_email_aluno'.");

        System.out.println("\nSetando variável 'recebeuEmail = true' para avançar...");
        runtimeService.setVariable(processInstance.getId(), "recebeuEmail", true);

        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        System.out.println("Verificando se o processo (após recarregar) está parado em 'gateway_defesa_aprovada'...");
        assertThat(processInstance).isActive().isWaitingAt("gateway_defesa_aprovada");
        System.out.println("Processo está corretamente esperando em 'gateway_defesa_aprovada'.");


        System.out.println("\nEnviando mensagem 'SolicitacaoUpload'...");

        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        System.out.println("Verificando se o processo está parado em 'Event_1y9v7kt'...");
        assertThat(processInstance).isActive().isWaitingAt("Event_1y9v7kt");
        System.out.println("Processo está corretamente esperando em 'Event_1y9v7kt'.");

        runtimeService.createMessageCorrelation("SolicitacaoUpload")
                .processInstanceId(processInstance.getProcessInstanceId())
                .correlate();

        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        System.out.println("verificando se o processo (após recarregar) está parado em 'Gateway_0l0yv3h'...");
        assertThat(processInstance).isActive().isWaitingAt("Gateway_0l0yv3h");
        System.out.println("Processo está corretamente esperando em 'Gateway_0l0yv3h'.");

        System.out.println("\nSetando variável 'recebeuEmail2 = true' para finalizar...");
        runtimeService.setVariable(processInstance.getId(), "recebeuEmail2", true);

        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        System.out.println("Verificando se o processo foi concluído...");
        assertThat(processInstance).isEnded();
        System.out.println("Processo finalizado com sucesso!");

        System.out.println("\nTESTE CONCLUÍDO");
    }
}