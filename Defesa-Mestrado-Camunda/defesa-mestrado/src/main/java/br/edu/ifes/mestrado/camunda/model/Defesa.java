package br.edu.ifes.mestrado.camunda.model;

import br.edu.ifes.mestrado.camunda.model.Banca;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.List;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Defesa {
    private int idDefesa;
    private int idAluno;
    private String dataDefesa;
    private String horaDefesa;
    private String localDefesa;
    private String tituloTrabalho;

    /**
     * Constrói um objeto Defesa com as informações da defesa fornecidas.
     *
     * @param dataDefesa A data da defesa.
     * @param horaDefesa A hora da defesa.
     * @param localDefesa O local onde a defesa será realizada.
     * @param tituloTrabalho O título do trabalho que será defendido.
     *
     **/

    public Defesa(int idAluno,String dataDefesa, String horaDefesa, String localDefesa, String tituloTrabalho) {
        this.idAluno = idAluno;
        this.dataDefesa = dataDefesa;
        this.horaDefesa = horaDefesa;
        this.localDefesa = localDefesa;
        this.tituloTrabalho = tituloTrabalho;
    }

    public Timestamp combinarDataHora() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dataDefesa + " " + horaDefesa, formatter);
            return Timestamp.valueOf(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public void setIdDefesa(int idDefesa) {
        this.idDefesa = idDefesa;
    }

    public void setDataDefesa(String dataDefesa) {
        this.dataDefesa = dataDefesa;
    }

    public void setHoraDefesa(String horaDefesa) {
        this.horaDefesa = horaDefesa;
    }

    public void setLocalDefesa(String localDefesa) {
        this.localDefesa = localDefesa;
    }

    public void setTituloTrabalho(String tituloTrabalho) {
        this.tituloTrabalho = tituloTrabalho;
    }


    public int getIdDefesa() {
        return idDefesa;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public String getDataDefesa() {
        return dataDefesa;
    }

    public String getHoraDefesa() {
        return horaDefesa;
    }

    public String getLocalDefesa() {
        return localDefesa;
    }

    public String getTituloTrabalho() {
        return tituloTrabalho;
    }

}