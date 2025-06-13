package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaConfirmacaoDefesa implements PromptPergunta {
    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaConfirmacaoDefesa(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body)throws Exception {
        try{
            String prompt = "Você é um assistente de análise de texto preciso e rigoroso.\n"
                    + "Analise o texto a seguir, que contém um 'Titulo do sistema' e um 'Corpo do Email'.\n\n"
                    + "Texto para análise: ```" + body + "```\n\n"
                    + "Siga estas duas tarefas:\n"
                    + "1. TAREFA DE CONSISTÊNCIA: Verifique se o 'Corpo do Email' menciona ou se refere diretamente ao 'Titulo do sistema'.\n"
                    + "2. TAREFA DE CONFIRMAÇÃO: Verifique se o 'Corpo do Email' contém uma mensagem explícita de confirmação, aceite ou intenção de prosseguir com a defesa.\n\n"
                    + "AVALIAÇÃO FINAL: Responda 'True' apenas se o resultado de AMBAS as tarefas (1 E 2) for positivo. Caso contrário, responda 'False'.\n"
                    + "Responda estritamente com a palavra True ou False.";

            String resposta = geminiAPI.perguntar(prompt);

            if (resposta.toLowerCase().contains("true")) {
                return "True";
            } else {
                return "False";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean booleanTakeQuestion(String text) throws Exception {
        String resposta = takeQuestion(text);

        if(resposta.equals("True")){
            return true;
        }
        return false;
    }
}
