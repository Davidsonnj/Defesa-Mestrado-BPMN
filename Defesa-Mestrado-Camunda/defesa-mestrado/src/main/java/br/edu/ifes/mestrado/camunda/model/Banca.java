package br.edu.ifes.mestrado.camunda.model;

public class Banca {
    private int idBanca;
    private String nome;
    private String email;
    private String instituicao;
    private String minicurriculo;

    /**
     * Constrói um objeto Banca com as informações fornecidas.
     *
     * @param nome O nome do membro da banca.
     * @param email O e-mail do membro da banca.
     * @param instituicao A instituição onde o membro da banca trabalha.
     * @param minicurriculo Um resumo breve (minicurrículo) sobre o membro da banca.
     *
     **/

    public Banca(String nome, String email, String instituicao, String minicurriculo) {
        this.nome = nome;
        this.email = email;
        this.instituicao = instituicao;
        this.minicurriculo = minicurriculo;
    }

    public int getIdBanca() {
        return idBanca;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public String getMinicurriculo() {
        return minicurriculo;
    }

    public void setIdBanca(int idBanca) {
        this.idBanca = idBanca;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public void setMinicurriculo(String minicurriculo) {
        this.minicurriculo = minicurriculo;
    }

}
