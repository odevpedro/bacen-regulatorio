package com.bacen.regulatorio.drex.valueobject;

import java.math.BigDecimal;

/**
 * Reserva de liquidez para operacoes no DREX.
 *
 * @param cpfCnpjTitular   CPF/CNPJ do titular
 * @param saldoDisponivel  saldo disponivel em DREX
 * @param saldoBloqueado   saldo bloqueado para operacoes em andamento
 */
public record ReservaLiquidez(String cpfCnpjTitular, BigDecimal saldoDisponivel, BigDecimal saldoBloqueado) {

    public ReservaLiquidez {
        if (saldoDisponivel == null || saldoDisponivel.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo disponivel nao pode ser negativo");
        }
        if (saldoBloqueado == null || saldoBloqueado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo bloqueado nao pode ser negativo");
        }
    }

    public BigDecimal saldoTotal() {
        return saldoDisponivel.add(saldoBloqueado);
    }

    public boolean possuiLiquidez(BigDecimal valor) {
        return saldoDisponivel.compareTo(valor) >= 0;
    }
}
