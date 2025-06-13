package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaValidacaoAnuencia implements PromptPergunta {
    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaValidacaoAnuencia(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String takeQuestion(String body) throws Exception {
        try {
            // O prompt é detalhado para garantir que a IA entenda a tarefa com precisão.
            String prompt = "Você é um assistente de IA preciso, especializado em comparar informações textuais.\n\n"
                    + "Sua tarefa é analisar o texto a seguir. Ele contém o assunto e o corpo de um e-mail, além de um 'Título da dissertação no sistema' para referência. Você deve verificar se o título mencionado no e-mail (seja no assunto ou no corpo) é o mesmo ou semanticamente equivalente ao título de referência do sistema.\n\n"
                    + "Texto para análise: ```" + body + "```\n\n"
                    + "## Instruções:\n"
                    + "1. **Identifique o Título de Referência:** Localize o 'Titulo da dissertação no sistema'.\n"
                    + "2. **Identifique o Título no E-mail:** Encontre o título da dissertação mencionado no 'Subject' ou no 'Body' do e-mail.\n"
                    + "3. **Compare:** Verifique se o título do e-mail corresponde ao título de referência. A correspondência é válida mesmo com pequenas variações de palavras ou abreviações (ex: 'Sistema de IA' vs. 'Sistema de Inteligência Artificial').\n\n"
                    + "## Exemplos:\n"
                    + "- **CASO 1 (MATCH):** Se o e-mail fala sobre 'Análise de Redes Neurais' e o título de referência é 'Análise de Redes Neurais', a resposta é True.\n"
                    + "- **CASO 2 (MATCH SEMÂNTICO):** Se o e-mail fala sobre 'anuência para o trabalho de IA para processamento de linguagem' e o título de referência é 'Uso de Inteligência Artificial para Processamento de Linguagem Natural', a resposta é True.\n"
                    + "- **CASO 3 (NO MATCH):** Se o e-mail fala sobre 'Análise de Sentimentos' e o título de referência é 'Sistema de Recomendação com Machine Learning', a resposta é False.\n\n"
                    + "Responda APENAS com a palavra `True` se os títulos forem correspondentes, ou `False` caso contrário.";

            String resposta = geminiAPI.perguntar(prompt);

            if (resposta.toLowerCase().contains("true")) {
                return "True";
            } else {
                return "False";
            }
        } catch (Exception e) {
            System.err.println("Erro ao validar título com IA: " + e.getMessage());
            return "False";
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
