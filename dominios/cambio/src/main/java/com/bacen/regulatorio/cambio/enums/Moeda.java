package com.bacen.regulatorio.cambio.enums;

/**
 * Codigos ISO 4217 — moedas com relevancia para operacoes de cambio no Brasil.
 * Res. BCB 277/2022.
 */
public enum Moeda {
    BRL("Real", "R$", 2),
    USD("Dolar Americano", "US$", 2),
    EUR("Euro", "EUR", 2),
    GBP("Libra Esterlina", "GBP", 2),
    JPY("Iene Japones", "JPY", 0),
    ARS("Peso Argentino", "ARS", 2),
    CHF("Franco Suico", "CHF", 2),
    CAD("Dolar Canadense", "CAD", 2),
    AUD("Dolar Australiano", "AUD", 2),
    CNY("Yuan Chines", "CNY", 2);

    private final String nome;
    private final String simbolo;
    private final int casasDecimais;

    Moeda(String nome, String simbolo, int casasDecimais) {
        this.nome = nome;
        this.simbolo = simbolo;
        this.casasDecimais = casasDecimais;
    }

    public String getNome() { return nome; }
    public String getSimbolo() { return simbolo; }
    public int getCasasDecimais() { return casasDecimais; }
}
