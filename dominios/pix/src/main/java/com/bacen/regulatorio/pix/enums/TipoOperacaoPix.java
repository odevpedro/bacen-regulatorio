package com.bacen.regulatorio.pix.enums;

/**
 * Resolução BCB 1/2020 — modalidades de transação no SPI.
 * Cada modalidade corresponde a uma mensagem ISO 20022 distinta.
 */
public enum TipoOperacaoPix {
    /** Pagamento padrão — pacs.008 no SPI. */
    PAGAMENTO,
    /** Devolução total ou parcial — pacs.004 no SPI. */
    DEVOLUCAO,
    /** Pagamento com QR Code estático — valor livre ou fixo. */
    QR_ESTATICO,
    /** Pagamento com QR Code dinâmico — cobrança com validade e instruções. */
    QR_DINAMICO,
    /** Pix agendado para data futura. */
    AGENDADO,
    /** Pix automático (débito recorrente autorizado). */
    AUTOMATICO
}
