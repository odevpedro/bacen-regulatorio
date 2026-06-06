package com.bacen.regulatorio.cambio.valueobject;

import com.bacen.regulatorio.cambio.enums.Moeda;

import java.math.BigDecimal;

/**
 * Res. BCB 277/2022 — taxa de cambio (PTAX ou taxa contratual).
 *
 * @param moedaBase    moeda base da cotacao (ex: USD)
 * @param moedaCotacao moeda de cotacao (ex: BRL)
 * @param valorTaxa    valor da taxa de cambio
 * @param tipoTaxa     "PTAX" (oficial BCB) ou "CONTRATUAL" (negociada entre as partes)
 */
public record TaxaCambio(Moeda moedaBase, Moeda moedaCotacao, BigDecimal valorTaxa, String tipoTaxa) {

    public TaxaCambio {
        if (moedaBase == null || moedaCotacao == null) {
            throw new IllegalArgumentException("Moeda base e moeda de cotacao sao obrigatorias");
        }
        if (moedaBase == moedaCotacao) {
            throw new IllegalArgumentException("Moeda base e moeda de cotacao devem ser diferentes");
        }
        if (valorTaxa == null || valorTaxa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa de cambio deve ser positiva");
        }
        if (!"PTAX".equals(tipoTaxa) && !"CONTRATUAL".equals(tipoTaxa)) {
            throw new IllegalArgumentException("Tipo de taxa deve ser PTAX ou CONTRATUAL");
        }
    }

    /**
     * Calcula o valor convertido usando esta taxa.
     * @param valorBase valor na moeda base
     * @return valor convertido para a moeda de cotacao
     */
    public BigDecimal converter(BigDecimal valorBase) {
        return valorBase.multiply(valorTaxa);
    }
}
