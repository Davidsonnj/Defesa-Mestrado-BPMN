package br.edu.ifes.mestrado.emailAPI.service;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaRequester {

    private final RestTemplate restTemplate = new RestTemplate();

    public void iniciarProcesso(String aluno, String tituloTrabalho, String emailAluno, String emailOrientador) {
        String url = "http://localhost:8080/engine-rest/condition";

        Map<String, Object> body = new HashMap<>();
        body.put("businessKey", "aluno-" + aluno.hashCode());

        Map<String, Object> variables = new HashMap<>();
        variables.put("email", criarVariavel(1, "Integer"));
        variables.put("aluno", criarVariavel(aluno, "String"));
        variables.put("titulo_trabalho", criarVariavel(tituloTrabalho, "String"));
        variables.put("emailAluno", criarVariavel(emailAluno, "String"));
        variables.put("emailOrientador", criarVariavel(emailOrientador, "String"));

        body.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Requisição enviada ao Camunda com sucesso!");
            System.out.println("Resposta: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Erro ao enviar requisição para o Camunda: " + e.getMessage());
        }
    }

    private Map<String, Object> criarVariavel(Object valor, String tipo) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", valor);
        var.put("type", tipo);
        return var;
    }
}
