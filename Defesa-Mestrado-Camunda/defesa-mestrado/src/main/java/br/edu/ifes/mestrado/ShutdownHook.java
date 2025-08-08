package br.edu.ifes.mestrado;

import jakarta.annotation.PreDestroy;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHook {

    @Autowired
    private ProcessEngine processEngine;

    @PreDestroy
    public void shutdown() {
        JobExecutor jobExecutor = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration().getJobExecutor();
        jobExecutor.shutdown();
    }
}

