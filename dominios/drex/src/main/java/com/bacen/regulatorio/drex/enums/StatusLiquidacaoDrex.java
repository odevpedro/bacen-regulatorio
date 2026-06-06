package com.bacen.regulatorio.drex.enums;

/**
 * Status de liquidacao de transacoes no DREX.
 */
public enum StatusLiquidacaoDrex {
    PENDENTE_LIQUIDACAO,
    LIQUIDACAO_ATOMICA_CONCLUIDA,
    LIQUIDACAO_PARCIAL,
    FALHA_LIQUIDACAO,
    REVERTIDA
}
