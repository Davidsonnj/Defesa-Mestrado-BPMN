package br.edu.ifes.mestrado.defesamestrado;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao.MsgConfirmacaoDelegate;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@SpringBootTest
public class SistemaDefesaCoordenador {

    @Autowired
    private RuntimeService runtimeService;

    @MockBean
    private MsgConfirmacaoDelegate msgConfirmacaoDelegate;

    @Test
    @Deployment(resources = {"BPMNs/Coordenacao.bpmn"})
    @Transactional
    public void testeCaminhoCompleto() {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("SolicitacaoAnuencia");

        assertThat(processInstance).isNotNull().isActive();

        assertThat(processInstance).isWaitingAt("Gateway_06c6ncz");

        runtimeService.setVariable(processInstance.getId(), "recebeuEmail", true);

        assertThat(processInstance).isEnded();
    }
}