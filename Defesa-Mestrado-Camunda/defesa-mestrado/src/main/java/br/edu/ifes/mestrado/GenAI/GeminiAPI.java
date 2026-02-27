package br.edu.ifes.mestrado.GenAI;

import com.google.genai.Client;
import com.google.genai.errors.ServerException;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public final class GeminiAPI {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String apiKey = dotenv.get("GEMINI_API_KEY");

    private static final Client client = Client.builder()
            .apiKey(apiKey)
            .httpOptions(HttpOptions.builder().apiVersion("v1").build())
            .build();

    public String perguntar(String pergunta) throws Exception {
        int retries = 0;
        int maxRetries = 2;
        long waitTime = 5000;

        while (true) {
            try {
                GenerateContentResponse response = client.models.generateContent(
                        "gemini-flash-latest",
                        pergunta,
                        null
                );
                return response.text();

            } catch (ServerException e) {
                if (retries >= maxRetries) {
                    throw new RuntimeException("Falha após várias tentativas: " + e.getMessage(), e);
                }
                retries++;
                System.out.println("Erro 503, tentativa " + retries + " de " + maxRetries + ". Retentando em " + waitTime + "ms");
                Thread.sleep(waitTime);
                waitTime *= 2;

            } catch (Exception e) {
                throw e;
            }
        }
    }
}
