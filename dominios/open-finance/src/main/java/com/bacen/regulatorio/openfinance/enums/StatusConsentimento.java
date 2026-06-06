package com.bacen.regulatorio.openfinance.enums;

/**
 * Resolução BCB 32/2020 — ciclo de vida do consentimento.
 * Status conforme especificação das APIs do Open Finance Brasil.
 */
public enum StatusConsentimento {
    /** Aguardando autorização do usuário. */
    AWAITING_AUTHORISATION,
    /** Autorizado e ativo. */
    AUTHORISED,
    /** Rejeitado pelo usuário ou pela instituição. */
    REJECTED,
    /** Consentimento utilizado (fluxo de iniciação de pagamento). */
    CONSUMED,
    /** Revogado pelo usuário. */
    REVOKED
}
