package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaDadosIniciais implements PromptPergunta {

    // O GeminiAPI continua injetado normalmente.
    private final GeminiAPI geminiAPI;

    // A única alteração foi aqui no prompt.
    private static final String PROMPT_UNIFICADO_TEMPLATE = """
        Você é um assistente de IA especialista em extrair e estruturar informações de e-mails acadêmicos.

        Sua tarefa é analisar o texto do e-mail abaixo e extrair TODAS as informações sobre a dissertação e a defesa de mestrado.

        **E-mail para Análise:**
        ```
        %s
        ```

        **Modelo de Saída (Formato Obrigatório):**
        Sua resposta deve seguir EXATAMENTE esta estrutura. Preencha cada campo com a informação correspondente encontrada no texto.

        - Tipo de Defesa: [retorne "qualificacao" para Exame de Qualificação ou "defesa" para Defesa de Dissertação]
        - Aluno: [nome completo do aluno]
        - Email do Aluno: [email do aluno]
        - Título da Dissertação: [título da dissertação]

        - Data da Defesa: [data no formato dd/mm/yyyy]
        - Hora da Defesa: [hora da defesa]
        - Local da Defesa: [local da defesa]
        
        - Coorientador nome: [nome do coorientador]
        - Coorientador email: [email do coorientador]

        - Banca Examinadora:
        - Nome: [nome do membro 1]
        - Email: [email do membro 1]
        - Instituição: [instituição do membro 1]
        - Minicurrículo: [minicurrículo do membro 1]

        - Nome: [nome do membro 2]
        - Email: [email do membro 2]
        - Instituição: [instituição do membro 2]
        - Minicurrículo: [minicurrículo do membro 2]

        (Repita o bloco "Nome, Email, Instituição, Minicurrículo" para quantos membros da banca forem encontrados)


        **Regras Finais (Cruciais):**
        1.  **Formato da Data:** A data DEVE ser formatada como `dd/mm/yyyy`. Se o ano não for especificado no texto, assuma o ano corrente (2025).
        2.  **Informação Ausente:** Se qualquer informação não for encontrada no texto, deixe o campo correspondente VAZIO, mas mantenha o rótulo (ex: `- Aluno: `).
        3.  **Sem Texto Adicional:** NÃO inclua NENHUMA introdução, explicação, comentário ou a palavra "null" na sua resposta. A saída deve ser apenas o texto formatado como no modelo.
  
        """;

    @Autowired
    public PerguntaDadosIniciais(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception {
        try {
            String promptFinal = String.format(PROMPT_UNIFICADO_TEMPLATE, body);
            return geminiAPI.perguntar(promptFinal);
        } catch (Exception e) {
            throw e;
        }
    }
}