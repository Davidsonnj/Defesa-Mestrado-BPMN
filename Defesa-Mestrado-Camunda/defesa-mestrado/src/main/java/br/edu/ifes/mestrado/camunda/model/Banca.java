package br.edu.ifes.mestrado.camunda.model;

public class Banca {
    private String nome;
    private String email;
    private String instituicao;
    private String minicurriculo;

    public Banca(String nome, String email, String instituicao, String minicurriculo) {
        this.nome = nome;
        this.email = email;
        this.instituicao = instituicao;
        this.minicurriculo = minicurriculo;
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
