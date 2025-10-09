package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaVinculacao implements PromptPergunta {

    private final GeminiAPI geminiAPI;

    private static final String PROMPT_VINCULACAO_TEMPLATE = """
        Você é um assistente de IA especialista em extrair e estruturar informações de e-mails acadêmicos.

        Sua tarefa é analisar o texto do e-mail abaixo e extrair TODAS as informações sobre a solicitação de vinculação de aluno a orientador.

        **E-mail para Análise:**
        ```
        %s
        ```

        **Modelo de Saída (Formato Obrigatório):**
        - Aluno: [nome completo do aluno]
        - Email do Aluno: [email do aluno]
        - Matrícula: [Matricula do aluno]
        - Período de Início: [Periodo que iniciou]
        - Tema: [Tema do curso]

        **Regras Finais (Cruciais):**
        1. **Todas as informações precisam ser encontradas no texto.
        2. **Formato da Data:** A data DEVE ser formatada como `dd/mm/yyyy`. Se o ano não for especificado no texto, assuma o ano corrente (2025).
        3. NÃO inclua explicações, comentários ou texto adicional fora do modelo.
    """;

    @Autowired
    public PerguntaVinculacao(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception {
        try {
            String promptFinal = String.format(PROMPT_VINCULACAO_TEMPLATE, body);
            return geminiAPI.perguntar(promptFinal);
        } catch (Exception e) {
            throw e;
        }
    }
}
