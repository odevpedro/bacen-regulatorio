package com.bacen.regulatorio.recebiveis.enums;

/**
 * Circular 4.016/2020 — Art. 2°, IX.
 * Define se o recebível pode ser transferido entre credenciadoras distintas.
 */
public enum IndicadorInteroperabilidade {
    /** Sem interoperabilidade — recebível vinculado à credenciadora original. */
    SI,
    /** Com interoperabilidade — permite transferência entre credenciadoras. */
    CI,
    /** Não informado. */
    NI
}
