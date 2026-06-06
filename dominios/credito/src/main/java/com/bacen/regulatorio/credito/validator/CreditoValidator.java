package com.bacen.regulatorio.credito.validator;

import com.bacen.regulatorio.credito.enums.TipoCredito;
import com.bacen.regulatorio.credito.valueobject.CET;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Res. 4.558/2017 + Res. 4.966/2021 — regras de validacao de operacoes de credito.
 *
 * Limites regulatorios:
 *   - CDC: prazo maximo 60 meses
 *   - Credito pessoal: taxa de juros limitada (conforme regulacao vigente)
 *   - CET deve ser informado obrigatoriamente na contratacao
 *   - Tarifas limitadas a lista taxativa da Res. 4.558
 */
public final class CreditoValidator {

    public static final int PRAZO_MAXIMO_CDC_MESES = 60;
    public static final int PRAZO_MAXIMO_CREDITO_PESSOAL_MESES = 96;

    private CreditoValidator() {}

    /**
     * Res. 4.558 — valida o prazo maximo por tipo de credito.
     */
    public static Optional<String> validarPrazoMaximo(TipoCredito tipo, int prazoMeses) {
        int limite = switch (tipo) {
            case CREDITO_PESSOAL, CREDITO_CONSIGNADO -> PRAZO_MAXIMO_CREDITO_PESSOAL_MESES;
            case FINANCIAMENTO_BENS -> PRAZO_MAXIMO_CDC_MESES;
            case FINANCIAMENTO_IMOBILIARIO -> 420;
            case CREDITO_ROTATIVO -> 12;
            case MICROCREDITO -> 24;
            default -> PRAZO_MAXIMO_CDC_MESES;
        };
        if (prazoMeses > limite) {
            return Optional.of("Prazo de " + prazoMeses + " meses excede o limite de " + limite + " meses para " + tipo);
        }
        return Optional.empty();
    }

    /**
     * Res. 4.558 — CET deve ser informado e nao pode exceder o limite legal.
     */
    public static Optional<String> validarCET(CET cet, BigDecimal limitePercentualAnual) {
        if (cet == null) {
            return Optional.of("CET e obrigatorio na contratacao");
        }
        if (limitePercentualAnual != null
                && cet.valorPercentualAnual().compareTo(limitePercentualAnual) > 0) {
            return Optional.of("CET de " + cet.valorPercentualAnual() + "% excede o limite legal");
        }
        return Optional.empty();
    }

    /**
     * Res. 4.558 — valor minimo de financiamento.
     */
    public static Optional<String> validarValorMinimo(BigDecimal valor, TipoCredito tipo) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Valor do financiamento deve ser positivo");
        }
        return Optional.empty();
    }
}
