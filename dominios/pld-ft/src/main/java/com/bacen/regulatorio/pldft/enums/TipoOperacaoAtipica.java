package com.bacen.regulatorio.pldft.enums;

/**
 * Circular BCB 3.978/2020 — Anexo I (situações que merecem atenção especial).
 * Operações que devem ser monitoradas e, se confirmadas, comunicadas ao COAF.
 */
public enum TipoOperacaoAtipica {
    /** Depósito ou saque em espécie acima de R$ 2.000 (exige registro). */
    ESPECIE_ACIMA_LIMITE,
    /** Espécie acima de R$ 10.000 (exige comunicação ao COAF). */
    ESPECIE_COMUNICACAO_COAF,
    /** Fracionamento de operações para evitar os limites de comunicação. */
    FRACIONAMENTO_SUSPEITO,
    /** Movimentação incompatível com perfil econômico declarado. */
    INCOMPATIVEL_PERFIL,
    /** Operações com países ou territórios de alto risco (lista GAFI). */
    JURISDICAO_ALTO_RISCO,
    /** Operações realizadas por Pessoa Exposta Politicamente (PEP). */
    PEP,
    /** Tentativas de anonimização da origem dos recursos. */
    OCULTACAO_ORIGEM,
    /** Operação sem propósito econômico aparente. */
    SEM_PROPOSITO_ECONOMICO
}
