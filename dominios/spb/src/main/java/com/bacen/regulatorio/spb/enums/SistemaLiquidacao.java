package com.bacen.regulatorio.spb.enums;

/**
 * Lei 10.214/2001 — sistemas de liquidacao do SPB.
 */
public enum SistemaLiquidacao {
    LBTR("Liquidacao Bruta em Tempo Real", "Sistema de transferencia de alto valor, liquidacao imediata"),
    STR("Sistema de Transferencia de Reservas", "Reservas bancarias entre instituicoes"),
    SILOC("Sistema de Liquidacao de Operacoes de Credito", "Operacoes de credito e compensacao"),
    C3("Câmara de Custodia e Liquidacao", "Ativos financeiros e custodia");

    private final String nome;
    private final String descricao;

    SistemaLiquidacao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}
