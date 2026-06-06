package com.bacen.regulatorio.cambio.validator;

import com.bacen.regulatorio.cambio.enums.FinalidadeCambio;
import com.bacen.regulatorio.cambio.enums.Moeda;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Res. BCB 277/2022 + Circ. 3.691/2013 — regras de validacao de operacoes de cambio.
 *
 * Limites regulatorios:
 *   - Especie em moeda nacional >= R$ 2.000: registro obrigatorio
 *   - Especie em moeda nacional >= R$ 10.000: comunicacao ao COAF
 *   - Operacao com pais nao cooperante: comunicacao obrigatoria
 */
public final class CambioValidator {

    public static final BigDecimal LIMITE_REGISTRO_ESPECIE = new BigDecimal("2000.00");
    public static final BigDecimal LIMITE_COMUNICACAO_COAF = new BigDecimal("10000.00");

    private CambioValidator() {}

    /**
     * Res. BCB 277 — valida se a moeda e elegivel para operacao de cambio no Brasil.
     */
    public static Optional<String> validarMoedaElegivel(Moeda moeda) {
        if (moeda == Moeda.BRL) {
            return Optional.of("BRL nao pode ser moeda de origem em operacao de cambio");
        }
        return Optional.empty();
    }

    /**
     * Res. BCB 277 — valor minimo para registro de operacao de cambio.
     */
    public static Optional<String> validarValorMinimo(BigDecimal valor, Moeda moeda) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Valor da operacao deve ser positivo");
        }
        return Optional.empty();
    }

    /**
     * Circ. 3.691 — prazo de liquidacao maximo por finalidade.
     */
    public static Optional<String> validarPrazoLiquidacao(
            FinalidadeCambio finalidade, long diasParaLiquidacao) {
        long limite = switch (finalidade) {
            case VIAGEM_INTERNACIONAL -> 30;
            case PAGAMENTO_IMPORTACAO -> 360;
            case RECEBIMENTO_EXPORTACAO -> 360;
            case REMESSA_LUCRO_DIVIDENDOS -> 30;
            case INVESTIMENTO_ESTRANGEIRO_DIRETO -> 60;
            case OPERACAO_CREDITO_EXTERNO -> 30;
            default -> 180;
        };
        if (diasParaLiquidacao > limite) {
            return Optional.of("Prazo de liquidacao excede o limite de " + limite + " dias para " + finalidade);
        }
        return Optional.empty();
    }

    /**
     * Res. BCB 277 — validacao de taxa PTAX vs taxa contratual.
     * A taxa contratual nao pode variar mais que X% da taxa PTAX do dia anterior.
     */
    public static Optional<String> validarVariacaoTaxa(
            BigDecimal taxaContratual, BigDecimal taxaPtax, BigDecimal limiteVariacaoPercentual) {
        if (taxaPtax.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Taxa PTAX invalida");
        }
        BigDecimal diferenca = taxaContratual.subtract(taxaPtax)
                .abs()
                .divide(taxaPtax, 6, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        if (diferenca.compareTo(limiteVariacaoPercentual) > 0) {
            return Optional.of("Variacao de " + diferenca + "% excede o limite de " + limiteVariacaoPercentual + "%");
        }
        return Optional.empty();
    }
}
