package com.bacen.regulatorio.openfinance.enums;

/**
 * Fases de implementação do Open Finance Brasil — Resolução BCB 32/2020.
 */
public enum FaseOpenFinance {
    /**
     * Fase 1 (fev/2021) — compartilhamento de dados públicos das instituições:
     * produtos, canais, taxas.
     */
    FASE_1,
    /**
     * Fase 2 (ago/2021) — compartilhamento de dados cadastrais e transacionais
     * do cliente mediante consentimento.
     */
    FASE_2,
    /**
     * Fase 3 (out/2021) — iniciação de pagamento (Pix) via Open Finance.
     */
    FASE_3,
    /**
     * Fase 4 (dez/2021) — compartilhamento de dados de câmbio, investimentos,
     * seguros e previdência.
     */
    FASE_4
}
