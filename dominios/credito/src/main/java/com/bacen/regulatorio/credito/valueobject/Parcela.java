package com.bacen.regulatorio.credito.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Res. 4.558/2017 — parcela de uma operacao de credito.
 *
 * @param numero     numero da parcela
 * @param dataVencimento data de vencimento
 * @param valor      valor da parcela
 * @param valorAmortizacao valor de amortizacao
 * @param valorJuros valor de juros
 * @param valorSeguro valor de seguro (se aplicavel)
 */
public record Parcela(
        int numero,
        LocalDate dataVencimento,
        BigDecimal valor,
        BigDecimal valorAmortizacao,
        BigDecimal valorJuros,
        BigDecimal valorSeguro
) {
    public Parcela {
        if (numero <= 0) throw new IllegalArgumentException("Numero da parcela deve ser positivo");
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da parcela deve ser positivo");
        }
    }
}
