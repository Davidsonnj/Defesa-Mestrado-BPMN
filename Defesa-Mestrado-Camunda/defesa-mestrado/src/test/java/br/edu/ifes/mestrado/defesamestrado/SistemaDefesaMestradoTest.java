package br.edu.ifes.mestrado.defesamestrado;

import br.edu.ifes.mestrado.camunda.model.Banca;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@SpringBootTest
public class SistemaDefesaMestradoTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    @Deployment(resources = {"BPMNs/SistemaDeMestrado.bpmn"})
    @Transactional
    public void testeCaminhoPrincipalCompleto() {

        Map<String, Object> variables = new HashMap<>();
        variables.put("aluno", "Davidson Silva");
        variables.put("titulo_trabalho", "Análise de Processos com BPMN e Camunda");
        variables.put("emailAluno", "davidsonifes@gmail.com");
        variables.put("dataDefesa", "2025/12/25");
        variables.put("horaDefesa", "14:00");
        variables.put("localDefesa", "Sala de Conferências A");

        Banca membro1 = new Banca("Prof. Dr. Carlos", "davidsoncsantos45@gmail.com", "Instituição A", "Especialista em IA.");
        Banca membro2 = new Banca("Profa. Dra. Ana", "davidsoncarvalhos45@gmail.com", "Instituição B", "Especialista em Engenharia de Software.");

        List<Banca> bancaDefesa = Arrays.asList(membro1, membro2);
        variables.put("bancaDefesa", bancaDefesa);

        variables.put("emailOrientador", "davidsoncs45@gmail.com");
        variables.put("nomeOrientador", "Dr. Orientador Principal");
        variables.put("nomeCoorientador", "Dr. Coorientador Secundario");
        variables.put("emailCoorientador", "davidsonifes@gmail.com");
        variables.put("idDadosIniciais", 167L);
        variables.put("idConfirmacaoDefesa", 162L);
        variables.put("caminhosDosAnexos", "/home/davidson/Desktop/1752573732108.jpeg");

        String businessKey = "defesa-davidson-" + System.currentTimeMillis();

        ProcessInstance processo = runtimeService.createMessageCorrelation("dadosAlunos")
                .processInstanceBusinessKey(businessKey)
                .setVariables(variables)
                .correlateWithResult().getProcessInstance();
        System.out.println("1) Processo com id: " + processo.getId() + "iniciado com sucesso");

        assertThat(processo).isWaitingAt("Gateway_0pcbsom");

        runtimeService.createMessageCorrelation("DefesaConfirmada")
                .processInstanceId(processo.getId())
                .correlate();
        System.out.println("2) Recebe msg de confirmação do aluno");

        assertThat(processo).isWaitingAt("recebe_anuencia");
        System.out.println("3) Processo esperando por anuencia");

        runtimeService.createMessageCorrelation("MsgCoordenacao")
                .processInstanceId(processo.getId())
                .setVariable("anuencia", true)
                .correlate();
        System.out.println("4) Recebe msg de aprovação da anuencia da defesa");

        assertThat(processo).isWaitingAt("Gateway_06yzxi6", "Gateway_1h17pz0");
        System.out.println("5) Processo aguardando em paralelo corretamente.");

        runtimeService.createMessageCorrelation("RegistrarDissertacao")
                .processInstanceId(processo.getId())
                .correlate();
        System.out.println("6) Recebe a dissertação do aluno");

        assertThat(processo).hasPassed("gerar_documentos");
        assertThat(processo).isEnded();

        System.out.println("Teste passou com sucesso!");
    }
}