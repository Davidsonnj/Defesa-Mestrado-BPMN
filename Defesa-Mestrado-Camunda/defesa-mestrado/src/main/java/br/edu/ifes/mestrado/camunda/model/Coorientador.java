package br.edu.ifes.mestrado.camunda.model;

public class Coorientador {
    private int idCoorientador;
    private String nome;
    private String email;

    /**
     * Constrói um objeto Coorientador com o nome e o e-mail fornecidos.
     *
     * @param nome O nome do coorientador.
     * @param email O e-mail do coorientador.
     *
     **/
    public Coorientador(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    public int getIdCoorientador() {
        return idCoorientador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setIdCoorientador(int idAluno) {
        this.idCoorientador = idAluno;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
