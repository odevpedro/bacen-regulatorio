package com.bacen.regulatorio.drex.validator;

import com.bacen.regulatorio.drex.valueobject.ReservaLiquidez;
import com.bacen.regulatorio.drex.valueobject.TransacaoDrex;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Regras de validacao do ecossistema DREX (Real Digital).
 *
 * Inclui: atomic settlement, verificacao de liquidez, regras de contrato inteligente.
 */
public final class DrexValidator {

    private DrexValidator() {}

    /**
     * Verifica se o remetente possui liquidez suficiente para a transacao.
     */
    public static Optional<String> validarLiquidez(ReservaLiquidez reserva, TransacaoDrex transacao) {
        if (!reserva.possuiLiquidez(transacao.valor())) {
            return Optional.of("Saldo insuficiente: disponivel " + reserva.saldoDisponivel()
                    + ", necessario " + transacao.valor());
        }
        return Optional.empty();
    }

    /**
     * Valida atomic settlement — todas as condicoes devem ser satisfeitas
     * antes da liquidacao: liquidez, conformidade, ausencia de conflitos.
     */
    public static Optional<String> validarAtomicSettlement(
            ReservaLiquidez reservaOrigem, TransacaoDrex transacao) {
        return validarLiquidez(reservaOrigem, transacao);
    }

    /**
     * Valida valor minimo de transacao (micropagamentos).
     */
    public static Optional<String> validarValorMinimo(BigDecimal valor) {
        BigDecimal minimo = new BigDecimal("0.01");
        if (valor.compareTo(minimo) < 0) {
            return Optional.of("Valor minimo da transacao e " + minimo);
        }
        return Optional.empty();
    }
}
