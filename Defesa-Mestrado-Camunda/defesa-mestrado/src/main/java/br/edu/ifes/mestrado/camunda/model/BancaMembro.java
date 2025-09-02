package br.edu.ifes.mestrado.camunda.model;

public class BancaMembro extends Banca {
    private boolean externo;

    public BancaMembro(Banca banca, boolean externo) {
        super(banca.getNome(), banca.getEmail(), banca.getInstituicao(), banca.getMinicurriculo());
        this.externo = externo;
        this.setIdBanca(banca.getIdBanca());
    }
    public BancaMembro(String nome, String email, String instituicao, String minicurriculo, boolean externo) {
        super(nome, email, instituicao, minicurriculo);
        this.externo = externo;
    }

    public boolean isExterno() {
        return externo;
    }

    public void setExterno(boolean externo) {
        this.externo = externo;
    }

    public String getTipoMembro() {
        return externo ? "Externo" : "Interno";
    }
}
