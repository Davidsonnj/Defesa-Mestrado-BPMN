package br.edu.ifes.mestrado.GenAI;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public final class GeminiAPI {
    private static final Client client = Client.builder()
            .apiKey("AIzaSyBu_ZZ3dO_qPL-cBr83dnBGB3YJafHHYXw")
            .build();

    public String perguntar(String pergunta) throws Exception{
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-1.5-flash",
                    pergunta,
                    null
            );
            return response.text();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}