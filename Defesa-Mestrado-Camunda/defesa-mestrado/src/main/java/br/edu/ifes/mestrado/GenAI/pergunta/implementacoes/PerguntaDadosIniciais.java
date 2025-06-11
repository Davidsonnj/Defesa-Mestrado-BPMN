package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaDadosIniciais implements PromptPergunta {

    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaDadosIniciais(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception{
        try {
            String texto = "Olá! Preciso que você me informe os seguintes dados para montar uma mensagem sobre a defesa de uma dissertação. "
                    + "Por favor, responda exatamente neste formato, para que eu possa extrair e armazenar em variáveis:\n\n"
                    + "aluno: nome do aluno   email: email do aluno   titulo: titulo da dissertação\n\n\n"
                    + "O texto de onde você deve extrair essas informações está a seguir:\n"
                    + body
                    + ". Se tu não encontrar, retornar SOMENTE a palavra null";

            return geminiAPI.perguntar(texto);
        } catch (Exception e) {
            throw e;
        }
    }

}
