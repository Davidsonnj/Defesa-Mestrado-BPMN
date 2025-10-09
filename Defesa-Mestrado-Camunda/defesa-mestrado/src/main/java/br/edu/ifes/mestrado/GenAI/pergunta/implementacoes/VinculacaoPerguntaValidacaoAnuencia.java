package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import br.edu.ifes.mestrado.camunda.controller.delegates.coordenacao.VinculacaoBuscarEmailAnuenciaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VinculacaoPerguntaValidacaoAnuencia implements PromptPergunta {
    private final GeminiAPI geminiAPI;

    @Autowired
    public VinculacaoPerguntaValidacaoAnuencia(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String takeQuestion(String body) throws Exception {
        try {
            // O prompt é detalhado para garantir que a IA entenda a tarefa com precisão.
            String prompt = "Você é um assistente de IA especializado em análise de contexto de mensagens.\n\n"
                    + "Sua tarefa é analisar o texto a seguir. Ele contém o assunto e o corpo de um e-mail. "
                    + "Você deve identificar se esse e-mail corresponde a uma autorização do coordenador para registrar no sistema a vinculação de orientação acadêmica (orientador ou coorientador).\n\n"
                    + "Texto para análise: ```" + body + "```\n\n"
                    + "## Instruções:\n"
                    + "1. **Verifique o Contexto:** Leia o 'Subject' e o 'Body' do e-mail.\n"
                    + "2. **Identifique o Papel do Coordenador:** Confirme se o texto indica que o coordenador está autorizando a vinculação de orientação.\n"
                    + "3. **Determine se é Autorização para Registro:** A mensagem deve indicar anuência, aceite, aprovação ou autorização explícita para registrar a vinculação no sistema.\n\n"
                    + "## Exemplos:\n"
                    + "- **CASO 1 (MATCH):** O e-mail diz 'Autorizo o registro no sistema da orientação do aluno João com o professor Carlos' → Resposta: True.\n"
                    + "- **CASO 2 (MATCH):** O e-mail diz 'Confirmo a anuência para vinculação de Maria como coorientadora no sistema' → Resposta: True.\n"
                    + "- **CASO 3 (NO MATCH):** O e-mail fala apenas sobre a entrega de documentos, sem mencionar autorização de vinculação → Resposta: False.\n"
                    + "- **CASO 4 (NO MATCH):** O e-mail menciona o orientador, mas sem nenhuma autorização para registro no sistema → Resposta: False.\n\n"
                    + "Responda APENAS com a palavra `True` se o e-mail indicar autorização do coordenador para registrar a vinculação de orientação no sistema, ou `False` caso contrário.";


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