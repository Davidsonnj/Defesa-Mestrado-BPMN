package br.edu.ifes.mestrado.emailAPI.model;

public class ExtrairDadosVinculacao {

    public static class DadosVinculacao {
        public String alunoNome;
        public String matricula;
        public String emailAluno;
        public String tema;
        public String periodoInicio;

        public DadosVinculacao(String alunoNome, String matricula, String emailAluno, String tema, String periodoInicio) {
            this.alunoNome = alunoNome;
            this.matricula = matricula;
            this.emailAluno = emailAluno;
            this.tema = tema;
            this.periodoInicio = periodoInicio;
        }

        @Override
        public String toString() {
            return "DadosVinculacao:\n" +
                    "  Nome do Aluno: " + alunoNome + "\n" +
                    "  Matrícula: " + matricula + "\n" +
                    "  Email do Aluno: " + emailAluno + "\n" +
                    "  Tema: " + tema + "\n" +
                    "  Período de Início: " + periodoInicio;
        }
    }

    public static DadosVinculacao extrair(String texto) {
        if (texto == null || texto.isBlank()) return null;

        String alunoNome = null, matricula = null, emailAluno = null, tema = null, periodoInicio = null;

        String[] linhas = texto.split("\n");
        for (String linha : linhas) {
            String t = linha.trim();
            if (t.startsWith("- Aluno:")) alunoNome = t.replace("- Aluno:", "").trim();
            else if (t.startsWith("- Matrícula:")) matricula = t.replace("- Matrícula:", "").trim();
            else if (t.startsWith("- Email do Aluno:")) emailAluno = t.replace("- Email do Aluno:", "").trim();
            else if (t.startsWith("- Tema:")) tema = t.replace("- Tema:", "").trim();
            else if (t.startsWith("- Período de Início:")) periodoInicio = t.replace("- Período de Início:", "").trim();
        }

        return new DadosVinculacao(alunoNome, matricula, emailAluno, tema, periodoInicio);
    }
}
