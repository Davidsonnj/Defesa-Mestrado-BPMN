package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import br.edu.ifes.mestrado.camunda.model.BancaMembro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaMembroExterno implements PromptPergunta {

    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaMembroExterno(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception {
        String promptFinal = """
            Você é um agente de IA que precisa identificar se um membro da banca é interno ou externo ao 
            Instituto Federal do Espírito Santo (IFES), Campus Serra.
            
            - Se o membro for do IFES Campus Serra → retorne apenas a palavra "false".
            - Se o membro for de qualquer outra instituição ou de outro campus do IFES → retorne apenas a palavra "true".
            
            Dados do membro:
            """ + body;

        return geminiAPI.perguntar(promptFinal).trim().toLowerCase();
    }

    public void takeQuestionMembroExterno(BancaMembro bancaMembro) {
        String prompt = String.format("""
            Nome: %s
            Instituição: %s
            Minicurrículo: %s
            """,
                bancaMembro.getNome(),
                bancaMembro.getInstituicao(),
                bancaMembro.getMinicurriculo()
        );

        try {
            String resposta = takeQuestion(prompt);

            bancaMembro.setExterno(resposta.equalsIgnoreCase("true"));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar se membro é externo", e);
        }
    }
}
