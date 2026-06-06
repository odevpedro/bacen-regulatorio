package com.bacen.regulatorio.drex.enums;

/**
 * Tipos de ativo digital no ecossistema DREX.
 */
public enum TipoAtivoDigital {
    DREX("Real Digital — CBDC brasileiro"),
    TOKEN_CAMBIAL("Token representativo de cambio"),
    TOKEN_CREDITO("Token de operacao de credito"),
    TITULO_PUBLICO("Titulo publico tokenizado"),
    TITULO_PRIVADO("Titulo privado tokenizado");

    private final String descricao;
    TipoAtivoDigital(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
