package br.edu.ifes.mestrado.camunda.model;

public class VinculacaoAluno {

    private String alunoNome;
    private String matricula;
    private String emailAluno;
    private String tema;
    private String periodoInicio;
    private long emailId;

    public long getEmailId() {
        return emailId;
    }

    public void setEmailId(long emailId) {
        this.emailId = emailId;
    }

    public String getPeriodoInicio() {
        return periodoInicio;
    }

    public String getTema() {
        return tema;
    }

    public String getEmailAluno() {
        return emailAluno;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setPeriodoInicio(String periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setEmailAluno(String emailAluno) {
        this.emailAluno = emailAluno;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }
    public VinculacaoAluno() {}

    public VinculacaoAluno(String nome, String matricula, String emailAluno, String periodoInicio, String tema) {
        this.alunoNome = nome;
        this.matricula = matricula;
        this.emailAluno = emailAluno;
        this.periodoInicio = periodoInicio;
        this.tema = tema;

    }
}
