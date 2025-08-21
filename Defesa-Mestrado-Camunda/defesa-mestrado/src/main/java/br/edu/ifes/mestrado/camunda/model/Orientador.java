package br.edu.ifes.mestrado.camunda.model;

public class Orientador {
    private int idOrientador;
    private String nome;
    private String email;

    /**
     * Constrói um objeto Orientador com o nome e o e-mail fornecidos.
     *
     * @param nome O nome do orientador.
     * @param email O e-mail do orientador.
     *
     **/
    public Orientador(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
    public int getIdorientador() {
        return idOrientador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setIdOrientador(int idOrientador) {
        this.idOrientador = idOrientador;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
