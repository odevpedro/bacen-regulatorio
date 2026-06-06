package com.bacen.regulatorio.arranjos.enums;

/**
 * Resolução CMN 4.282/2013 — papéis dos participantes em arranjos de pagamento.
 */
public enum TipoParticipante {
    /** Institui e regulamenta as regras do arranjo (ex: Visa, Mastercard, Elo). */
    INSTITUIDOR,
    /** Emite instrumento de pagamento ao usuário final (cartão de crédito/débito). */
    EMISSOR,
    /** Credencia e habilita estabelecimentos comerciais a aceitar o instrumento. */
    CREDENCIADOR,
    /** Realiza liquidação e compensação das transações entre os participantes. */
    LIQUIDANTE,
    /** Usuário final pagador. */
    PAGADOR,
    /** Usuário final recebedor (estabelecimento comercial). */
    RECEBEDOR
}
