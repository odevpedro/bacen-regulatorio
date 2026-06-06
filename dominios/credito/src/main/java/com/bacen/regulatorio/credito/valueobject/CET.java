package com.bacen.regulatorio.credito.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Res. 4.558/2017 — Custo Efetivo Total (CET) de uma operacao de credito.
 * Representa o custo total da operacao em percentual anual, incluindo juros, tarifas, tributos e seguros.
 *
 * @param valorPercentualAnual CET em percentual anual
 * @param valorMonetario       CET em valor monetario total
 */
public record CET(BigDecimal valorPercentualAnual, BigDecimal valorMonetario) {

    private static final BigDecimal CEM = new BigDecimal("100");

    public CET {
        if (valorPercentualAnual == null || valorPercentualAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("CET percentual deve ser >= 0");
        }
        if (valorMonetario == null || valorMonetario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("CET monetario deve ser >= 0");
        }
    }

    /**
     * Calcula o CET a partir dos componentes.
     *
     * @param valorFinanciado   valor total financiado
     * @param totalPrestacoes   soma de todas as prestacoes
     * @param prazoMeses        prazo em meses
     */
    public static CET calcular(BigDecimal valorFinanciado, BigDecimal totalPrestacoes, int prazoMeses) {
        if (valorFinanciado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor financiado deve ser positivo");
        }
        if (prazoMeses <= 0) {
            throw new IllegalArgumentException("Prazo deve ser positivo");
        }
        BigDecimal jurosTotal = totalPrestacoes.subtract(valorFinanciado);
        BigDecimal taxaEfetivaMensal = jurosTotal
                .divide(valorFinanciado, 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(prazoMeses), 10, RoundingMode.HALF_UP);
        BigDecimal taxaAnual = taxaEfetivaMensal
                .multiply(BigDecimal.valueOf(12))
                .multiply(CEM);
        return new CET(taxaAnual.setScale(4, RoundingMode.HALF_UP), jurosTotal);
    }
}
