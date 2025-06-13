package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaAnuencia implements PromptPergunta {

    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaAnuencia(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception {
        String prompt = "Analise o seguinte e-mail e decida se a anuência foi concedida.\n\n"
                + "E-mail: ```" + body + "```\n\n"
                + "Responda da seguinte forma, e de nenhuma outra:\n"
                + "- Se a anuência for positiva, responda APENAS com a palavra: Autorizado\n"
                + "- Se a anuência for negada, responda com o formato EXATO: Rejeitado, Justificativa: [explique o motivo aqui]\n";

        return geminiAPI.perguntar(prompt);
    }

    public String anuenciaQuestion(String body) throws Exception {
        String resposta = takeQuestion(body);

        if (resposta.trim().equals("Autorizado")) {
            return "autorizado";
        } else {
            return resposta;
        }
    }
}