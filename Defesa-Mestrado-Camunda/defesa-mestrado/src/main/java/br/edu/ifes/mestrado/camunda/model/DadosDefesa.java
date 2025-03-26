package br.edu.ifes.mestrado.camunda.model;

import br.edu.ifes.mestrado.camunda.model.Banca;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;

public class DadosDefesa {
    private String aluno;
    private String titulo;
    private Date data;
    private String hora;
    private String local;
    private Banca banca;

    // Construtor com os campos obrigatórios (aluno e titulo)
    public DadosDefesa(String aluno, String titulo) {
        this.aluno = aluno;
        this.titulo = titulo;
    }

    // Métodos setters e getters para os campos opcionais
    public void setData(Date data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    public String getAluno() {
        return aluno;
    }

    public String getTitulo() {
        return titulo;
    }

    public Date getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getLocal() {
        return local;
    }

    public Banca getBanca() {
        return banca;
    }
}