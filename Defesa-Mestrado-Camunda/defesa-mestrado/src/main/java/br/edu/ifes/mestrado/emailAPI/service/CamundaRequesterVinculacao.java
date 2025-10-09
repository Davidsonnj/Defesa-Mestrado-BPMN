package br.edu.ifes.mestrado.emailAPI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaRequesterVinculacao {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean iniciarProcessoVinculacao(
            String alunoNome,
            String matricula,
            String emailAluno,
            String tema,
            String periodoInicio,
            String emailOrientador,
            String nomeOrientador,
            Long idEmail
    ) {
        if (alunoNome == null || matricula == null || emailAluno == null || tema == null || periodoInicio == null) {
            System.err.println("Dados insuficientes para iniciar o processo de vinculação.");
            return false;
        }

        String url = "http://localhost:8080/engine-rest/condition";

        Map<String, Object> body = new HashMap<>();
        body.put("businessKey", "vinculacao-" + alunoNome.hashCode());

        Map<String, Object> vars = new HashMap<>();
        vars.put("alunoNome", criarVariavel(alunoNome, "String"));
        vars.put("matricula", criarVariavel(matricula, "String"));
        vars.put("emailAluno", criarVariavel(emailAluno, "String"));
        vars.put("tema", criarVariavel(tema, "String"));
        vars.put("periodoInicio", criarVariavel(periodoInicio, "String"));
        vars.put("emailOrientador", criarVariavel(emailOrientador, "String"));
        vars.put("idEmail", criarVariavel(idEmail, "Long"));

        body.put("variables", vars);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String jsonBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            System.out.println("JSON Vinculação:\n" + jsonBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Erro ao enviar requisição de vinculação: " + e.getMessage());
            return false;
        }
    }

    private Map<String, Object> criarVariavel(Object valor, String tipo) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", valor);
        var.put("type", tipo);
        return var;
    }

    private Map<String, Object> criarVariavelJson(String valorJson) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", valorJson);
        var.put("type", "Json");
        return var;
    }

}
