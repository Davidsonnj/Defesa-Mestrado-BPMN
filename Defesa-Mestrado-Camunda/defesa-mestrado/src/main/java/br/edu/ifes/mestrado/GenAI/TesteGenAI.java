package br.edu.ifes.mestrado.GenAI;

public class TesteGenAI {

    public static void main(String[] args) {
        GeminiAPI geminiAPI = new GeminiAPI();
        String body = "Assunto: Defesa de Dissertação – Análise dos Impactos das Redes Sociais na Educação\n" +
                "\n" +
                "Prezados,\n" +
                "\n" +
                "Gostaria de informar que o aluno Carlos Eduardo Silva realizará a defesa da sua dissertação intitulada \"Análise dos Impactos das Redes Sociais na Educação\".\n" +
                "\n" +
                "Data e horário da defesa serão informados em breve.\n" +
                "\n" +
                "Para eventuais dúvidas ou contato, o e-mail do aluno é: carlos.silva@email.com.\n" +
                "\n" +
                "Atenciosamente,\n" +
                "Coordenação do Programa de Mestrado\n";

        String pergunta = "Olá! Preciso que você me informe os seguintes dados para montar uma mensagem sobre a defesa de uma dissertação. "
                + "Por favor, responda exatamente neste formato, para que eu possa extrair e armazenar em variáveis:\n\n"
                + "aluno: nome do aluno   email: email do aluno   titulo: titulo da dissertação\n\n\n"
                + "O texto de onde você deve extrair essas informações está a seguir:\n"
                + body;


        try {
            System.out.println(geminiAPI.perguntar(pergunta));
        }catch (Exception e) {
            System.err.println("Erro ao processar takeQuestion: " + e.getMessage());
        }

    }
}