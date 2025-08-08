package br.edu.ifes.mestrado.emailAPI.model;

import br.edu.ifes.mestrado.camunda.model.Banca;

import java.util.ArrayList;
import java.util.List;

public class ExtrairDadosEmail {

    /**
     * Classe interna para armazenar os dados extraídos do texto do e-mail.
     * Foi ajustada para ter um construtor completo e campos para todos os dados.
     */
    public static class DadosExtraidos {
        public String aluno;
        public String email;
        public String titulo;
        public String emailOrientador; // Este campo não está no prompt atual, mas mantido na classe.
        public String dataDefesa;
        public String horaDefesa;
        public String localDefesa;
        public List<Banca> banca;
        public String emailCoorientador;
        public String nomeCoorientador;

        // Construtor completo para facilitar a criação do objeto.
        public DadosExtraidos(String aluno, String email, String titulo, String dataDefesa, String horaDefesa, String localDefesa, String nomeCoorientador, String emailCoorientador,List<Banca> banca) {
            this.aluno = aluno;
            this.email = email;
            this.titulo = titulo;
            this.dataDefesa = dataDefesa;
            this.horaDefesa = horaDefesa;
            this.localDefesa = localDefesa;
            this.nomeCoorientador = nomeCoorientador;
            this.emailCoorientador = emailCoorientador;
            this.banca = banca;

        }
    }

    /**
     * Método principal que analisa uma String formatada (vinda da IA) e extrai os dados.
     *
     * @param textoFormatado A resposta completa e estruturada da API Gemini.
     * @return Um objeto DadosExtraidos preenchido, ou null se o texto for inválido.
     */
    public static DadosExtraidos extrairDados(String textoFormatado) {
        if (textoFormatado == null || textoFormatado.trim().isEmpty()) {
            return null;
        }

        // Variáveis para armazenar os dados de nível superior
        String aluno = null, email = null, titulo = null, dataDefesa = null, horaDefesa = null, localDefesa = null, nomeCoorientador = null, emailCoorientador = null;
        List<Banca> bancaList = new ArrayList<>();

        // Variáveis temporárias para construir cada membro da banca
        String nomeMembro = null, emailMembro = null, instituicaoMembro = null, miniCurriculoMembro = null;

        String[] lines = textoFormatado.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("- Aluno:")) {
                aluno = extractValue(trimmedLine, "- Aluno:");
            } else if (trimmedLine.startsWith("- Email do Aluno:")) {
                email = extractValue(trimmedLine, "- Email do Aluno:");
            } else if (trimmedLine.startsWith("- Título da Dissertação:")) {
                titulo = extractValue(trimmedLine, "- Título da Dissertação:");
            } else if (trimmedLine.startsWith("- Data da Defesa:")) {
                dataDefesa = extractValue(trimmedLine, "- Data da Defesa:");
            } else if (trimmedLine.startsWith("- Hora da Defesa:")) {
                horaDefesa = extractValue(trimmedLine, "- Hora da Defesa:");
            } else if (trimmedLine.startsWith("- Local da Defesa:")) {
                localDefesa = extractValue(trimmedLine, "- Local da Defesa:");
            } else if (trimmedLine.startsWith("- Coorientador nome:")) {
                nomeCoorientador = extractValue(trimmedLine, "- Coorientador nome:");
            }else if (trimmedLine.startsWith("- Coorientador email:")) {
                emailCoorientador = extractValue(trimmedLine, "- Coorientador email:");
            } else if (trimmedLine.startsWith("- Nome:")) {
                // Ao encontrar um novo "Nome", significa que um novo membro da banca começou.
                // Primeiro, salvamos o membro anterior se ele existir.
                if (nomeMembro != null) {
                    bancaList.add(new Banca(nomeMembro, emailMembro, instituicaoMembro, miniCurriculoMembro));
                }
                // Agora, começamos a coletar os dados do novo membro.
                nomeMembro = extractValue(trimmedLine, "- Nome:");
                // Resetamos os outros campos do membro para evitar usar dados antigos.
                emailMembro = null;
                instituicaoMembro = null;
                miniCurriculoMembro = null;
            } else if (trimmedLine.startsWith("- Email:")) {
                emailMembro = extractValue(trimmedLine, "- Email:");
            } else if (trimmedLine.startsWith("- Instituição:")) {
                instituicaoMembro = extractValue(trimmedLine, "- Instituição:");
            } else if (trimmedLine.startsWith("- Minicurrículo:")) {
                miniCurriculoMembro = extractValue(trimmedLine, "- Minicurrículo:");
            }
        }

        // Após o loop, é crucial salvar o último membro da banca que foi processado.
        if (nomeMembro != null) {
            // Presumindo que a classe Banca tenha um construtor que aceite estes campos.
            // Adapte se o construtor for diferente.
            bancaList.add(new Banca(nomeMembro, emailMembro, instituicaoMembro, miniCurriculoMembro));
        }

        return new DadosExtraidos(aluno, email, titulo, dataDefesa, horaDefesa, localDefesa, nomeCoorientador, emailCoorientador, bancaList);
    }

    /**
     * Helper para extrair o valor de uma linha, dado um prefixo.
     * Ex: extractValue("- Aluno: João da Silva", "- Aluno:") retorna "João da Silva".
     *
     * @param line    A linha completa a ser processada.
     * @param prefix  O rótulo/prefixo a ser removido.
     * @return O valor extraído, ou null se o valor estiver em branco.
     */
    private static String extractValue(String line, String prefix) {
        String value = line.substring(prefix.length()).trim();
        return value.isEmpty() ? null : value;
    }
}