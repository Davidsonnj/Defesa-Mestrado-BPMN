package br.edu.ifes.mestrado.camunda.model;

public class Aluno {
    private int idAluno;
    private String nome;
    private String email;

    /**
     * Constrói um objeto Aluno com o nome e o e-mail fornecidos.
     *
     * @param nome O nome do aluno.
     * @param email O e-mail do aluno.
     *
     **/
    public Aluno(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    public int getIdAluno() {
        return idAluno;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
