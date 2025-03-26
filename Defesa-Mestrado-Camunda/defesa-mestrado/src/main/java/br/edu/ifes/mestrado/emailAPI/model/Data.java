package br.edu.ifes.mestrado.emailAPI.model;

public class Data {
    private String dia;
    private String mes;
    private String ano;
    private String hora;
    private String minuto;

    public Data(String dia, String mes, String ano , String hora, String minuto) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
    }

    public String getDia() {
        return dia;
    }

    public String getMes() {
        return mes;
    }

    public String getAno() {
        return ano;
    }

    public String getHora() {
        return hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }


}
