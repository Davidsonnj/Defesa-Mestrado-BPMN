package br.edu.ifes.mestrado.camunda.controller;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin") // Endpoint base para tarefas administrativas
public class AdminController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    /**
     * Endpoint para apagar TODAS as instâncias de processo, ativas e históricas.
     * Use com CUIDADO. Apenas para ambiente de desenvolvimento.
     */
    @PostMapping("/cleanup-instances")
    public ResponseEntity<String> cleanupAllProcessInstances() {

        // --- Limpeza de Instâncias Ativas ---
        List<ProcessInstance> activeInstances = runtimeService.createProcessInstanceQuery().list();
        int activeCount = activeInstances.size();

        if (activeCount > 0) {
            List<String> activeInstanceIds = activeInstances.stream()
                    .map(ProcessInstance::getId)
                    .collect(Collectors.toList());
            // Usa o método de deleção em massa, que é mais eficiente
            runtimeService.deleteProcessInstances(activeInstanceIds, "Limpeza de ambiente de teste via endpoint", true, true);
        }

        // --- Limpeza de Instâncias Históricas (Finalizadas) ---
        List<HistoricProcessInstance> historicInstances = historyService.createHistoricProcessInstanceQuery().list();
        int historicCount = historicInstances.size();

        if (historicCount > 0) {
            List<String> historicInstanceIds = historicInstances.stream()
                    .map(HistoricProcessInstance::getId)
                    .collect(Collectors.toList());
            // Usa o método de deleção em massa do histórico
            historyService.deleteHistoricProcessInstancesBulk(historicInstanceIds);
        }

        String responseMessage = String.format(
                "Limpeza concluída. %d instâncias ativas e %d instâncias históricas foram apagadas.",
                activeCount,
                historicCount
        );

        return ResponseEntity.ok(responseMessage);
    }
}