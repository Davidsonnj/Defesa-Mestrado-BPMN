package br.edu.ifes.mestrado.GenAI.pergunta.implementacoes;

import br.edu.ifes.mestrado.GenAI.GeminiAPI;
import br.edu.ifes.mestrado.GenAI.pergunta.interfaces.PromptPergunta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerguntaDadosDetalhados implements PromptPergunta {

    private final GeminiAPI geminiAPI;

    @Autowired
    public PerguntaDadosDetalhados(GeminiAPI geminiAPI) {
        this.geminiAPI = geminiAPI;
    }

    @Override
    public String takeQuestion(String body) throws Exception{
        try {
            String prompt = "Você é um assistente de IA que extrai informações de e-mails e as formata em um texto simples e limpo.\n\n"
                    + "Analise o e-mail de resposta a seguir e extraia as informações sobre a defesa de mestrado.\n\n"
                    + "E-mail para análise: ```" + body + "```\n\n"
                    + "Formate a saída EXATAMENTE como no modelo abaixo, usando hífens, espaços e quebras de linha. Se houver mais de um membro na banca, repita o bloco de informações de cada membro.\n\n"
                    + "Modelo de Saída:\n"
                    + "- Data: [data extraída]\n"
                    + "- Hora: [hora extraída]\n"
                    + "- Local: [local extraído]\n\n"
                    + "- Banca:\n"
                    + "- Nome: [nome do membro 1]\n"
                    + "- Email: [email do membro 1]\n"
                    + "- Instituição: [instituição do membro 1]\n"
                    + "- Minicurrículo: [minicurrículo do membro 1]\n\n"
                    + "(se houver membro 2, repita o bloco)\n"
                    + "- Nome: [nome do membro 2]\n"
                    + "- Email: [email do membro 2]\n"
                    + "- Instituição: [instituição do membro 2]\n"
                    + "- Minicurrículo: [minicurrículo do membro 2]\n\n"
                    + "Regras Finais:\n"
                    + "- Se uma informação não for encontrada, deixe o campo em branco (ex: '- Data: ').\n"
                    + "- Não inclua NENHUM texto, explicação ou comentário adicional na sua resposta. A resposta deve ser apenas o texto formatado como no modelo.";

            return geminiAPI.perguntar(prompt);

        } catch (Exception e) {
            throw e;
        }
    }
}
