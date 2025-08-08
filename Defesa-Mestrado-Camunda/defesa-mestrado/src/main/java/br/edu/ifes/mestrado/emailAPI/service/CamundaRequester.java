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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean iniciarProcesso(
            String aluno,
            String tituloTrabalho,
            String emailAluno,
            String nomeOrientador,
            String emailOrientador,
            String dataDefesa,
            String horaDefesa,
            String localDefesa,
            String nomeCoorientador,
            String emailCoorientador,
            List<Banca> bancaDefesa
    ) {
        if(bancaDefesa.isEmpty() || aluno == null || tituloTrabalho == null || emailAluno == null ||
           emailOrientador == null || dataDefesa == null || horaDefesa == null || localDefesa == null)
        {
            System.err.println("Dados insuficientes para iniciar o processo.");
            return false;
        }
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
        variables.put("nomeOrientador", criarVariavel(nomeOrientador, "String"));
        variables.put("emailOrientador", criarVariavel(emailOrientador, "String"));
        variables.put("dataDefesa", criarVariavel(dataDefesa, "String"));
        variables.put("horaDefesa", criarVariavel(horaDefesa, "String"));
        variables.put("localDefesa", criarVariavel(localDefesa, "String"));
        variables.put("nomeCoorientador", criarVariavel(nomeCoorientador, "String"));
        variables.put("emailCoorientador", criarVariavel(emailCoorientador, "String"));

        // Serializar a banca como JSON puro
        try {
            String bancaJson = objectMapper.writeValueAsString(bancaDefesa);
            variables.put("bancaDefesa", criarVariavelJson(bancaJson));
        } catch (Exception e) {
            System.err.println("Erro ao serializar a banca: " + e.getMessage());
            return false;
        }

        body.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // --- CÓDIGO PARA VISUALIZAR O JSON SENDO ENVIADO ---
        try {
            System.out.println("================= CORPO DA REQUISIÇÃO JSON ==================");
            // Usa o objectMapper para criar um JSON formatado (pretty print)
            String jsonBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            System.out.println(jsonBody);
            System.out.println("==========================================================");
        } catch (Exception e) {
            System.err.println("Erro ao converter o corpo da requisição para JSON: " + e.getMessage());
        }
        // --- FIM DO TRECHO DE VISUALIZAÇÃO ---

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