package br.edu.ifes.mestrado.emailAPI.service;

import br.edu.ifes.mestrado.camunda.model.Banca;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CamundaRequester {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para serializar a lista

    public boolean iniciarProcesso(
            String aluno,
            String tituloTrabalho,
            String emailAluno,
            String emailOrientador,
            String dataDefesa,
            String horaDefesa,
            String localDefesa,
            List<Banca> banca
    ) {
        String url = "http://localhost:8080/engine-rest/condition";

        // Corpo da requisição
        Map<String, Object> body = new HashMap<>();
        body.put("businessKey", "aluno-" + aluno.hashCode());

        // Variáveis do processo
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", criarVariavel(1, "Integer"));
        variables.put("aluno", criarVariavel(aluno, "String"));
        variables.put("titulo_trabalho", criarVariavel(tituloTrabalho, "String"));
        variables.put("emailAluno", criarVariavel(emailAluno, "String"));
        variables.put("emailOrientador", criarVariavel(emailOrientador, "String"));
        variables.put("dataDefesa", criarVariavel(dataDefesa, "String"));
        variables.put("horaDefesa", criarVariavel(horaDefesa, "String"));
        variables.put("localDefesa", criarVariavel(localDefesa, "String"));

        // Serializar a banca como JSON puro
        try {
            String bancaJson = objectMapper.writeValueAsString(banca);
            variables.put("banca", criarVariavelJson(bancaJson));
        } catch (Exception e) {
            System.err.println("Erro ao serializar a banca: " + e.getMessage());
            return false;
        }

        body.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Requisição enviada ao Camunda com sucesso!");
            System.out.println("Resposta: " + response.getStatusCode());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Erro ao enviar requisição para o Camunda: " + e.getMessage());
            return false;
        }
    }

    // Cria variável comum
    private Map<String, Object> criarVariavel(Object valor, String tipo) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", valor);
        var.put("type", tipo);
        return var;
    }

    // Cria variável do tipo Json (Camunda não tenta desserializar)
    private Map<String, Object> criarVariavelJson(String valorJson) {
        Map<String, Object> var = new HashMap<>();
        var.put("value", valorJson);
        var.put("type", "Json"); // Camunda aceita como JSON bruto
        return var;
    }
}
