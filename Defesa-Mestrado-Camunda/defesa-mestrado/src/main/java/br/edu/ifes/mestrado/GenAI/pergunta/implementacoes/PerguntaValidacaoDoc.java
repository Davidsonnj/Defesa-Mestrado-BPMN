package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaValidacaoDoc implements PromptPergunta {
    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaValidacaoDoc(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    public String takeQuestion(String body) throws Exception {

        String prompt = "Você é um assistente de IA preciso, focado em comparar e validar títulos em textos.\n\n"
                + "Sua tarefa é analisar o texto fornecido. Ele contém um título de referência do sistema e o conteúdo de um e-mail (assunto e corpo). Você deve determinar se o título discutido no e-mail é o mesmo ou semanticamente equivalente ao título de referência.\n\n"
                + "Texto para Análise: ```" + body + "```\n\n"
                + "## Instruções:\n"
                + "1. **Identifique o 'Titulo do trabalho no sistema'.** Esta é a sua referência principal.\n"
                + "2. **Analise o 'Subject' e o 'Body' do e-mail** para encontrar o título do trabalho que está sendo discutido.\n"
                + "3. **Compare os dois títulos.** A correspondência é considerada válida mesmo que haja pequenas diferenças de palavras, abreviações ou ordem, desde que o tema central seja o mesmo (ex: 'Sistema de IA para finanças' é equivalente a 'Uso de Inteligência Artificial no mercado financeiro').\n\n"
                + "Responda estritamente com a palavra `True` se os títulos corresponderem, ou `False` se não corresponderem. Não adicione nenhuma outra palavra ou pontuação.";

        String resposta = geminiAPI.perguntar(prompt);

        if (resposta.toLowerCase().contains("true")) {
            return "True";
        } else {
            return "False";
        }
    }

    public boolean booleanTakeQuestion(String txt) throws Exception {
        String resposta = takeQuestion(txt);

        if(resposta.equals("True")){
            return true;
        }
        return false;
    }
}
