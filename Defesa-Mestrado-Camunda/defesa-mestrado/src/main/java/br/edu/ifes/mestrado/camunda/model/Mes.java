package br.edu.ifes.mestrado.camunda.model;

public enum Mes {
    JANEIRO(1, "Janeiro"),
    FEVEREIRO(2, "Fevereiro"),
    MARCO(3, "Março"),
    ABRIL(4, "Abril"),
    MAIO(5, "Maio"),
    JUNHO(6, "Junho"),
    JULHO(7, "Julho"),
    AGOSTO(8, "Agosto"),
    SETEMBRO(9, "Setembro"),
    OUTUBRO(10, "Outubro"),
    NOVEMBRO(11, "Novembro"),
    DEZEMBRO(12, "Dezembro");

    private final int numero;
    private final String nomePorExtenso;

    Mes(int numero, String nomePorExtenso) {
        this.numero = numero;
        this.nomePorExtenso = nomePorExtenso;
    }

    public String getNomePorExtenso() {
        return nomePorExtenso;
    }

    public static Mes fromNumero(int numero) {
        for (Mes mes : Mes.values()) {
            if (mes.numero == numero) {
                return mes;
            }
        }
        throw new IllegalArgumentException("Número do mês inválido: " + numero);
    }
}
