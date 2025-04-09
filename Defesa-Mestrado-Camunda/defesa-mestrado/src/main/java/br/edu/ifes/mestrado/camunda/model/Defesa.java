package br.edu.ifes.mestrado.camunda.model;

import br.edu.ifes.mestrado.camunda.model.Banca;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.List;

public class Defesa {
    private int idDefesa;
    private int idAluno;
    private String dataDefesa;
    private String horaDefesa;
    private String localDefesa;
    private String tituloTrabalho;
    private List<Integer> bancaDefesa; // Lista de IDs dos membros da banca

    public Defesa(int idAluno, String dataDefesa, String horaDefesa, String localDefesa, String tituloTrabalho, List<Integer> bancaDefesa) {
        this.idAluno = idAluno;
        this.dataDefesa = dataDefesa;
        this.horaDefesa = horaDefesa;
        this.localDefesa = localDefesa;
        this.tituloTrabalho = tituloTrabalho;
        this.bancaDefesa = bancaDefesa;
    }

}